package br.com.facens.atividade4.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO para respostas do AI Course Advisor.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AIChatResponseDTO {

    private String response;
}
