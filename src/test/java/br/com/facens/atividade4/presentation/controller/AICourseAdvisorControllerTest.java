package br.com.facens.atividade4.presentation.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.facens.atividade4.application.dto.AIChatRequestDTO;
import br.com.facens.atividade4.application.dto.AIChatResponseDTO;
import br.com.facens.atividade4.application.service.AICourseAdvisorApplicationService;

@WebMvcTest(AICourseAdvisorController.class)
@DisplayName("Testes para AICourseAdvisorController")
class AICourseAdvisorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AICourseAdvisorApplicationService aiService;

    @Test
    @DisplayName("Deve processar requisição de chat com sucesso")
    void deveProcessarRequisicaoDeChatComSucesso() throws Exception {
        AIChatRequestDTO request = new AIChatRequestDTO("Quais são os tipos de assinatura?");
        AIChatResponseDTO response = new AIChatResponseDTO("Temos Básica e Premium");

        when(aiService.chat(any(AIChatRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/ai/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Temos Básica e Premium"));

        verify(aiService).chat(any(AIChatRequestDTO.class));
    }

    @Test
    @DisplayName("Deve retornar 400 quando mensagem for vazia")
    void deveRetornar400QuandoMensagemForVazia() throws Exception {
        AIChatRequestDTO request = new AIChatRequestDTO("");

        when(aiService.chat(any(AIChatRequestDTO.class)))
            .thenThrow(new IllegalArgumentException("Mensagem não pode ser vazia"));

        mockMvc.perform(post("/api/ai/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve processar perguntas sobre alunos específicos")
    void deveProcessarPerguntasSobreAlunosEspecificos() throws Exception {
        AIChatRequestDTO request = new AIChatRequestDTO("Me fale sobre o aluno ID 1");
        AIChatResponseDTO response = new AIChatResponseDTO("João Silva tem 10 cursos");

        when(aiService.chat(any(AIChatRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/ai/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("João Silva tem 10 cursos"));
    }
}
