package br.com.facens.atividade4.presentation.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.facens.atividade4.application.dto.AlunoDTO;
import br.com.facens.atividade4.application.dto.AtualizarAlunoDTO;
import br.com.facens.atividade4.application.dto.CriarAlunoDTO;
import br.com.facens.atividade4.application.service.AlunoApplicationService;
import br.com.facens.atividade4.domain.TipoAssinatura;
import br.com.facens.atividade4.presentation.request.FinalizarCursoRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.mockito.Mockito;

// Testa o controller via MockMvc: CRUD e finalização de curso retornando status/JSON corretos.
@WebMvcTest(controllers = AlunoController.class)
@Import(AlunoControllerTest.ControllerTestConfig.class)
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AlunoApplicationService alunoService;

    @BeforeEach
    void resetMocks() {
        reset(alunoService);
    }

    @Test
    void deveListarAlunos() throws Exception {
        AlunoDTO aluno = AlunoDTO.builder()
                .id(1L)
                .nome("Aluno API")
                .email("aluno.api@example.com")
                .assinatura(TipoAssinatura.ASSINATURA_BASICA)
                .cursosDisponiveis(5)
                .build();

        when(alunoService.listarTodos()).thenReturn(List.of(aluno));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/alunos")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", equalTo("aluno.api@example.com")));
    }

    @Test
    void deveCriarAluno() throws Exception {
        CriarAlunoDTO request = new CriarAlunoDTO();
        request.setNome("Aluno Novo");
        request.setEmail("novo.aluno@example.com");
        request.setAssinatura(TipoAssinatura.ASSINATURA_PREMIUM);

        AlunoDTO response = AlunoDTO.builder()
                .id(10L)
                .nome("Aluno Novo")
                .email("novo.aluno@example.com")
                .assinatura(TipoAssinatura.ASSINATURA_PREMIUM)
                .cursosDisponiveis(10)
                .build();

        when(alunoService.criar(any(CriarAlunoDTO.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/alunos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", equalTo("http://localhost/api/alunos/10")))
                .andExpect(jsonPath("$.id", equalTo(10)));
    }

    @Test
    void deveAtualizarAluno() throws Exception {
        AtualizarAlunoDTO request = new AtualizarAlunoDTO();
        request.setNome("Aluno Atualizado");
        request.setEmail("aluno.atualizado@example.com");
        request.setAssinatura(TipoAssinatura.ASSINATURA_BASICA);

        AlunoDTO response = AlunoDTO.builder()
                .id(1L)
                .nome("Aluno Atualizado")
                .email("aluno.atualizado@example.com")
                .assinatura(TipoAssinatura.ASSINATURA_BASICA)
                .cursosDisponiveis(7)
                .build();

        when(alunoService.atualizar(eq(1L), any(AtualizarAlunoDTO.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/alunos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", equalTo("Aluno Atualizado")));
    }

    @Test
    void deveBuscarAlunoPorId() throws Exception {
        AlunoDTO response = AlunoDTO.builder()
                .id(7L)
                .nome("Aluno Consultado")
                .email("consulta@example.com")
                .assinatura(TipoAssinatura.ASSINATURA_PREMIUM)
                .cursosDisponiveis(11)
                .build();

        when(alunoService.buscarPorId(7L)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/alunos/7")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo("consulta@example.com")));
    }

    @Test
    void deveRemoverAluno() throws Exception {
        doNothing().when(alunoService).remover(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/alunos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveFinalizarCurso() throws Exception {
        FinalizarCursoRequest request = new FinalizarCursoRequest(8.5);

        AlunoDTO response = AlunoDTO.builder()
                .id(2L)
                .nome("Aluno Finalizado")
                .email("aluno.finalizado@example.com")
                .assinatura(TipoAssinatura.ASSINATURA_PREMIUM)
                .cursosDisponiveis(13)
                .build();

        when(alunoService.finalizarCurso(eq(2L), eq(8.5))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/alunos/2/finalizar-curso")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cursosDisponiveis", equalTo(13)));
    }
    @TestConfiguration
    static class ControllerTestConfig {

        @Bean
        @Primary
        AlunoApplicationService alunoApplicationService() {
            return Mockito.mock(AlunoApplicationService.class);
        }
    }
}
