package br.com.facens.atividade4.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.facens.atividade4.application.dto.AIChatRequestDTO;
import br.com.facens.atividade4.application.dto.AIChatResponseDTO;
import br.com.facens.atividade4.application.service.AICourseAdvisorApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller REST para o AI Course Advisor.
 * Expõe endpoints para interação com o consultor de cursos baseado em IA.
 */
@Validated
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/ai")
@Tag(name = "AI Course Advisor", description = "Consultor de cursos com Inteligência Artificial")
public class AICourseAdvisorController {

    private final AICourseAdvisorApplicationService aiService;

    @PostMapping("/chat")
    @Operation(
        summary = "Conversar com o AI Course Advisor",
        description = "Permite fazer perguntas sobre cursos, assinaturas, gamificação e alunos"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resposta da IA gerada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content)
    })
    public ResponseEntity<AIChatResponseDTO> chat(@Valid @RequestBody AIChatRequestDTO request) {
        try {
            AIChatResponseDTO response = aiService.chat(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
