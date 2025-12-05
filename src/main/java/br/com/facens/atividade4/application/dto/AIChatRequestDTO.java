package br.com.facens.atividade4.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO para requisições de chat com o AI Course Advisor.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AIChatRequestDTO {

    @NotBlank(message = "Mensagem não pode ser vazia")
    private String message;
}
