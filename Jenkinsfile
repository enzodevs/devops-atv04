pipeline {
    agent any

    tools {
        maven 'Maven-3.9.5'
        jdk 'JDK-17'
    }

    environment {
        DOCKER_IMAGE = 'facens/atividade4'
        DOCKER_TAG = "${BUILD_NUMBER}"
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
                bat 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo '========== Executando Testes =========='
                bat 'mvn test'
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
                bat 'mvn pmd:pmd pmd:cpd'
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
                bat 'mvn jacoco:prepare-agent test jacoco:report'
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
                    def jacocoReport = bat(
                        script: 'type target\\site\\jacoco\\index.html | findstr /C:"Total"',
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
                bat 'mvn clean package -DskipTests'
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
                bat "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                bat "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
            }
        }

        stage('Test Container') {
            steps {
                echo '========== Testando Container =========='
                script {
                    // Para container anterior se existir
                    bat "docker stop test-app-${BUILD_NUMBER} 2>nul || echo Container nao existe"
                    bat "docker rm test-app-${BUILD_NUMBER} 2>nul || echo Container nao existe"

                    // Inicia novo container de teste
                    bat "docker run -d --name test-app-${BUILD_NUMBER} -p 9090:8080 ${DOCKER_IMAGE}:${DOCKER_TAG}"

                    // Aguarda inicialização
                    sleep time: 30, unit: 'SECONDS'

                    // Testa se está respondendo
                    bat 'curl http://localhost:9090/actuator/health || echo "Servico nao respondeu"'

                    // Para e remove container de teste
                    bat "docker stop test-app-${BUILD_NUMBER}"
                    bat "docker rm test-app-${BUILD_NUMBER}"
                }
            }
        }

        stage('Deploy Staging') {
            when {
                branch 'develop'
            }
            steps {
                echo '========== Deploy para Staging =========='
                bat 'docker-compose -f docker-compose.staging.yml down'
                bat 'docker-compose -f docker-compose.staging.yml up -d --no-color'
                sleep time: 60, unit: 'SECONDS'
                bat 'docker-compose -f docker-compose.staging.yml ps'
                bat 'curl http://localhost:8686 || echo "Service not responding"'
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
                bat 'docker-compose -f docker-compose.prod.yml down'
                bat 'docker-compose -f docker-compose.prod.yml up -d --no-color'
                sleep time: 60, unit: 'SECONDS'
                bat 'docker-compose -f docker-compose.prod.yml ps'
                bat 'curl http://localhost:8585 || echo "Service not responding"'
            }
        }
    }

    post {
        always {
            echo '========== Limpeza =========='
            bat 'docker system prune -f 2>nul || echo "Nada para limpar"'
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