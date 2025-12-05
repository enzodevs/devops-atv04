package br.com.facens.atividade4.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.facens.atividade4.application.dto.AIChatRequestDTO;
import br.com.facens.atividade4.application.dto.AIChatResponseDTO;
import br.com.facens.atividade4.infrastructure.ai.AICourseAdvisorService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para AICourseAdvisorApplicationService")
class AICourseAdvisorApplicationServiceTest {

    @Mock
    private AICourseAdvisorService aiCourseAdvisor;

    @InjectMocks
    private AICourseAdvisorApplicationService service;

    @BeforeEach
    void setUp() {
        // Setup inicial se necessário
    }

    @Test
    @DisplayName("Deve processar pergunta do usuário e retornar resposta da IA")
    void deveProcessarPerguntaDoUsuario() {
        AIChatRequestDTO request = new AIChatRequestDTO("Quais são os tipos de assinatura?");
        when(aiCourseAdvisor.chat(request.getMessage()))
            .thenReturn("Temos dois tipos: Básica (5 cursos) e Premium (10 cursos)");

        AIChatResponseDTO response = service.chat(request);

        assertNotNull(response);
        assertNotNull(response.getResponse());
        assertTrue(response.getResponse().contains("Básica"));
        assertTrue(response.getResponse().contains("Premium"));
        verify(aiCourseAdvisor).chat(request.getMessage());
    }

    @Test
    @DisplayName("Deve lidar com mensagem vazia")
    void deveLidarComMensagemVazia() {
        AIChatRequestDTO request = new AIChatRequestDTO("");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.chat(request)
        );

        assertEquals("Mensagem não pode ser vazia", exception.getMessage());
        verify(aiCourseAdvisor, never()).chat(anyString());
    }

    @Test
    @DisplayName("Deve lidar com mensagem nula")
    void deveLidarComMensagemNula() {
        AIChatRequestDTO request = new AIChatRequestDTO(null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> service.chat(request)
        );

        assertEquals("Mensagem não pode ser vazia", exception.getMessage());
        verify(aiCourseAdvisor, never()).chat(anyString());
    }

    @Test
    @DisplayName("Deve processar corretamente perguntas sobre alunos específicos")
    void deveProcessarPerguntasSobreAlunosEspecificos() {
        AIChatRequestDTO request = new AIChatRequestDTO("Me fale sobre o aluno ID 1");
        when(aiCourseAdvisor.chat(request.getMessage()))
            .thenReturn("O aluno João Silva tem assinatura Premium e 10 cursos disponíveis");

        AIChatResponseDTO response = service.chat(request);

        assertNotNull(response);
        assertTrue(response.getResponse().contains("João Silva"));
        verify(aiCourseAdvisor).chat(request.getMessage());
    }
}
