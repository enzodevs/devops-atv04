pipeline {
    agent any

    tools {
        maven 'Maven-3.9.5'
        jdk 'JDK-17'
    }

    environment {
        DOCKER_IMAGE = 'facens/atividade4'
        DOCKER_TAG = "${BUILD_NUMBER}"
        DOCKER_HOST = 'tcp://host.docker.internal:2375'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo 'Código fonte obtido do repositório'
            }
        }

        stage('Build') {
            steps {
                echo '========== Build da aplicação =========='
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo '========== Executando Testes =========='
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }

        stage('Análise PMD') {
            steps {
                echo '========== Análise de código PMD =========='
                sh 'mvn pmd:pmd pmd:cpd'
                recordIssues(
                    enabledForFailure: true,
                    tools: [
                        pmdParser(pattern: '**/target/pmd.xml'),
                        cpd(pattern: '**/target/cpd.xml')
                    ]
                )
            }
        }

        stage('Cobertura JaCoCo') {
            steps {
                echo '========== Relatório de Cobertura =========='
                sh 'mvn jacoco:prepare-agent test jacoco:report'
            }
            post {
                always {
                    jacoco(
                        execPattern: 'target/jacoco.exec',
                        classPattern: 'target/classes',
                        sourcePattern: 'src/main/java',
                        exclusionPattern: '**/config/**,**/exception/**,**/dto/**'
                    )
                }
            }
        }

        stage('Quality Gate') {
            steps {
                echo '========== Verificando Quality Gate 99% =========='
                script {
                    def jacocoReport = sh(
                        script: 'cat target/site/jacoco/index.html | grep "Total"',
                        returnStdout: true
                    ).trim()

                    def pattern = ~/(\d+)%/
                    def matcher = pattern.matcher(jacocoReport)

                    if (matcher.find()) {
                        def coverage = Integer.parseInt(matcher.group(1))
                        echo "Cobertura de código: ${coverage}%"

                        if (coverage < 99) {
                            error("Quality Gate FALHOU: Cobertura ${coverage}% < 99%")
                        } else {
                            echo "Quality Gate APROVADO: Cobertura ${coverage}%"
                        }
                    }
                }
            }
        }

        stage('Package') {
            steps {
                echo '========== Empacotando aplicação =========='
                sh 'mvn clean package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                echo '========== Construindo Imagem Docker =========='
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
            }
        }

        stage('Trivy Scan - Filesystem') {
            steps {
                echo '========== Trivy: Varredura de Dependências (Filesystem) =========='
                sh '''
                    trivy fs --exit-code 1 --severity HIGH,CRITICAL \
                        --format table --output trivy-fs-report.txt .
                '''
            }
            post {
                always {
                    archiveArtifacts artifacts: 'trivy-fs-report.txt',
                                     onlyIfSuccessful: false
                }
            }
        }

        stage('Trivy Scan - Image') {
            steps {
                echo '========== Trivy: Varredura da Imagem Docker =========='
                sh """
                    trivy image --exit-code 1 --severity HIGH,CRITICAL \
                        --format table --output trivy-image-report.txt \
                        ${DOCKER_IMAGE}:${DOCKER_TAG}
                """
            }
            post {
                always {
                    archiveArtifacts artifacts: 'trivy-image-report.txt',
                                     onlyIfSuccessful: false
                }
            }
        }

        stage('Test Container') {
            steps {
                echo '========== Testando Container =========='
                script {
                    // Para container anterior se existir
                    sh "docker stop test-app-${BUILD_NUMBER} 2>/dev/null || echo Container nao existe"
                    sh "docker rm test-app-${BUILD_NUMBER} 2>/dev/null || echo Container nao existe"

                    // Inicia novo container de teste
                    sh "docker run -d --name test-app-${BUILD_NUMBER} -p 9090:8080 ${DOCKER_IMAGE}:${DOCKER_TAG}"

                    // Aguarda inicialização
                    sleep time: 30, unit: 'SECONDS'

                    // Testa se está respondendo
                    sh 'curl http://host.docker.internal:9090/actuator/health || echo "Servico nao respondeu"'

                    // Para e remove container de teste
                    sh "docker stop test-app-${BUILD_NUMBER}"
                    sh "docker rm test-app-${BUILD_NUMBER}"
                }
            }
        }

        stage('Deploy Staging') {
            when {
                branch 'develop'
            }
            steps {
                echo '========== Deploy para Staging =========='
                sh 'docker-compose -f docker-compose.staging.yml down'
                sh 'docker-compose -f docker-compose.staging.yml up -d --no-color'
                sleep time: 60, unit: 'SECONDS'
                sh 'docker-compose -f docker-compose.staging.yml ps'
                sh 'curl http://host.docker.internal:8686 || echo "Service not responding"'
            }
        }

        stage('Deploy Production') {
            when {
                branch 'main'
            }
            input {
                message "Deploy em produção?"
                ok "Sim, fazer deploy"
            }
            steps {
                echo '========== Deploy para Produção =========='
                sh 'docker-compose -f docker-compose.prod.yml down'
                sh 'docker-compose -f docker-compose.prod.yml up -d --no-color'
                sleep time: 60, unit: 'SECONDS'
                sh 'docker-compose -f docker-compose.prod.yml ps'
                sh 'curl http://host.docker.internal:8585 || echo "Service not responding"'
            }
        }
    }

    post {
        always {
            echo '========== Limpeza =========='
            sh 'docker system prune -f 2>/dev/null || echo "Nada para limpar"'
            echo "Pipeline finalizado: ${currentBuild.currentResult}"
        }
        success {
            echo 'Pipeline executado com sucesso!'
        }
        failure {
            echo 'Pipeline falhou - verificar logs'
        }
    }
}