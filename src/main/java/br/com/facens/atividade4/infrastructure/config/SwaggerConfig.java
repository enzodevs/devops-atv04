package br.com.facens.atividade4.infrastructure.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do SpringDoc OpenAPI para documentação Swagger.
 * Disponível em: http://localhost:8080/swagger-ui.html
 * JSON disponível em: http://localhost:8080/v3/api-docs
 */
@Configuration
public class SwaggerConfig {

    // Configura o OpenAPI/Swagger para expor documentação e servidores (localhost 8080/8686).
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestão de Alunos")
                        .version("1.0.0")
                        .description("""
                                API RESTful para gerenciamento de alunos com funcionalidades de:
                                - CRUD completo de alunos
                                - Sistema de gamificação com bônus por conclusão de cursos
                                - Gestão de assinaturas (FREE, PRO, PREMIUM)
                                - Validações de negócio e tratamento de erros

                                **Arquitetura:** Clean Architecture + DDD
                                **Framework:** Spring Boot 3.5.5
                                **Banco de Dados:** H2 (desenvolvimento)
                                """)
                        .contact(new Contact()
                                .name("Enzo")
                                .email("223335@facens.br"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("http://localhost:8686")
                                .description("Servidor Docker (Staging/Prod)")
                ))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação Completa do Projeto")
                        .url("https://github.com/seu-usuario/devops-atv04"));
    }
}
