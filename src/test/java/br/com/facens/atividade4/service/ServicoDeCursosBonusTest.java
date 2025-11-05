package br.com.facens.atividade4.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.facens.atividade4.domain.Aluno;
import br.com.facens.atividade4.domain.ContatoEmail;
import br.com.facens.atividade4.domain.TipoAssinatura;

class ServicoDeCursosBonusTest {

    private final ServicoDeCursos servico = new ServicoDeCursos();

    @Test
    void deveConsiderarBonusQuandoAprovado() {
        Aluno aluno = Aluno.novo("Aluno Bonus", ContatoEmail.of("bonus@example.com"),
                TipoAssinatura.ASSINATURA_BASICA);
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 8.0);

        assertTrue(aluno.getCursosDisponiveis() > cursosIniciais);
    }

    @Test
    void naoDeveConcederBonusQuandoNotaNaoAtingeCritico() {
        Aluno aluno = Aluno.novo("Aluno Sem Bonus", ContatoEmail.of("sembonus@example.com"),
                TipoAssinatura.ASSINATURA_BASICA);
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 3.0);

        assertEquals(cursosIniciais, aluno.getCursosDisponiveis());
    }

    @Test
    void naoDeveConcederBonusQuandoAlunoNaoPodeReceber() {
        Aluno aluno = Mockito.spy(Aluno.novo("Aluno Restrito", ContatoEmail.of("restrito@example.com"),
                TipoAssinatura.ASSINATURA_PREMIUM));
        Mockito.when(aluno.podeReceberBonus()).thenReturn(false);
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 9.0);

        assertEquals(cursosIniciais, aluno.getCursosDisponiveis());
        Mockito.verify(aluno).podeReceberBonus();
    }
}
