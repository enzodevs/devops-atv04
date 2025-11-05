package br.com.facens.atividade4;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

class Atividade4ApplicationMainTest {

    @Test
    void deveExecutarMetodoMain() {
        assertDoesNotThrow(() -> {
            try (ConfigurableApplicationContext context =
                         SpringApplication.run(Atividade4Application.class,
                                 "--spring.main.web-application-type=none",
                                 "--spring.main.banner-mode=off")) {
                // contexto iniciado e encerrado
            }
        });
    }
}
