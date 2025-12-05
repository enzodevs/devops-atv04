package br.com.facens.atividade4.infrastructure.ai;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.facens.atividade4.domain.Aluno;
import br.com.facens.atividade4.domain.ContatoEmail;
import br.com.facens.atividade4.domain.TipoAssinatura;
import br.com.facens.atividade4.infrastructure.persistence.AlunoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para AlunoTools - Ferramentas de IA para Alunos")
class AlunoToolsTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoTools alunoTools;

    private Aluno aluno;

    @BeforeEach
    void setUp() {
        ContatoEmail email = ContatoEmail.of("joao@example.com");
        aluno = Aluno.builder()
                .id(1L)
                .nome("João Silva")
                .contatoEmail(email)
                .tipoAssinatura(TipoAssinatura.ASSINATURA_PREMIUM)
                .cursosDisponiveis(10)
                .build();
    }

    @Test
    @DisplayName("Deve buscar informações do aluno por ID")
    void deveBuscarInformacoesDoAlunoPorId() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        String resultado = alunoTools.buscarInformacoesAluno(1L);

        assertNotNull(resultado);
        assertTrue(resultado.contains("João Silva"));
        assertTrue(resultado.contains("ASSINATURA_PREMIUM"));
        assertTrue(resultado.contains("10"));
        verify(alunoRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar mensagem quando aluno não existe")
    void deveRetornarMensagemQuandoAlunoNaoExiste() {
        when(alunoRepository.findById(999L)).thenReturn(Optional.empty());

        String resultado = alunoTools.buscarInformacoesAluno(999L);

        assertNotNull(resultado);
        assertTrue(resultado.contains("não encontrado"));
        verify(alunoRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve listar todos os alunos")
    void deveListarTodosOsAlunos() {
        when(alunoRepository.findAll()).thenReturn(List.of(aluno));

        String resultado = alunoTools.listarTodosAlunos();

        assertNotNull(resultado);
        assertTrue(resultado.contains("João Silva"));
        assertTrue(resultado.contains("1"));
        verify(alunoRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar mensagem quando não há alunos")
    void deveRetornarMensagemQuandoNaoHaAlunos() {
        when(alunoRepository.findAll()).thenReturn(List.of());

        String resultado = alunoTools.listarTodosAlunos();

        assertNotNull(resultado);
        assertTrue(resultado.contains("Nenhum aluno"));
        verify(alunoRepository).findAll();
    }

    @Test
    @DisplayName("Deve explicar tipos de assinatura")
    void deveExplicarTiposDeAssinatura() {
        String resultado = alunoTools.explicarTiposAssinatura();

        assertNotNull(resultado);
        assertTrue(resultado.contains("ASSINATURA_BASICA"));
        assertTrue(resultado.contains("ASSINATURA_PREMIUM"));
        assertTrue(resultado.contains("5"));
        assertTrue(resultado.contains("10"));
    }

    @Test
    @DisplayName("Deve explicar sistema de gamificação")
    void deveExplicarSistemaDeGamificacao() {
        String resultado = alunoTools.explicarGamificacao();

        assertNotNull(resultado);
        assertTrue(resultado.contains("Gamificação"));
        assertTrue(resultado.contains("bônus"));
    }
}
