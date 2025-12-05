package br.com.facens.atividade4.application.service;

import org.springframework.stereotype.Service;

import br.com.facens.atividade4.application.dto.AIChatRequestDTO;
import br.com.facens.atividade4.application.dto.AIChatResponseDTO;
import br.com.facens.atividade4.infrastructure.ai.AICourseAdvisorService;
import lombok.RequiredArgsConstructor;

/**
 * Serviço de aplicação para o AI Course Advisor.
 * Orquestra a comunicação com o serviço de IA.
 */
@Service
@RequiredArgsConstructor
public class AICourseAdvisorApplicationService {

    private final AICourseAdvisorService aiCourseAdvisor;

    public AIChatResponseDTO chat(AIChatRequestDTO request) {
        validarMensagem(request.getMessage());

        String aiResponse = aiCourseAdvisor.chat(request.getMessage());
        return new AIChatResponseDTO(aiResponse);
    }

    private void validarMensagem(String message) {
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Mensagem não pode ser vazia");
        }
    }
}
