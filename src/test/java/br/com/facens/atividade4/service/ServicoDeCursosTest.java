package br.com.facens.atividade4.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.facens.atividade4.domain.Aluno;
import br.com.facens.atividade4.domain.ContatoEmail;
import br.com.facens.atividade4.domain.TipoAssinatura;

class ServicoDeCursosTest {

    private ServicoDeCursos servico;

    @BeforeEach
    void setUp() {
        servico = new ServicoDeCursos();
    }

    @Test
    void deveAdicionarCursosBonusParaAlunoAprovado() {
        Aluno aluno = novoAluno(TipoAssinatura.ASSINATURA_BASICA, "aprovado@example.com");
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 8.0);

        assertEquals(cursosIniciais + 3, aluno.getCursosDisponiveis());
    }

    @Test
    void deveAdicionarCursosBonusParaNotaLimite() {
        Aluno aluno = novoAluno(TipoAssinatura.ASSINATURA_PREMIUM, "limite@example.com");
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 7.0);

        assertEquals(cursosIniciais + 3, aluno.getCursosDisponiveis());
    }

    @Test
    void naoDeveAdicionarCursosBonusParaAlunoReprovado() {
        Aluno aluno = novoAluno(TipoAssinatura.ASSINATURA_BASICA, "reprovado@example.com");
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 6.9);

        assertEquals(cursosIniciais, aluno.getCursosDisponiveis());
    }

    @Test
    void naoDeveAdicionarCursosBonusParaNotaZero() {
        Aluno aluno = novoAluno(TipoAssinatura.ASSINATURA_PREMIUM, "nota.zero@example.com");
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 0.0);

        assertEquals(cursosIniciais, aluno.getCursosDisponiveis());
    }

    @Test
    void deveAdicionarCursosBonusParaNotaMaxima() {
        Aluno aluno = novoAluno(TipoAssinatura.ASSINATURA_BASICA, "nota.maxima@example.com");
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 10.0);

        assertEquals(cursosIniciais + 3, aluno.getCursosDisponiveis());
    }

    @Test
    void deveLancarExcecaoParaAlunoNulo() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> servico.finalizarCurso(null, 8.0)
        );

        assertEquals("Aluno não pode ser nulo", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoParaNotaNegativa() {
        Aluno aluno = novoAluno(TipoAssinatura.ASSINATURA_BASICA, "nota.negativa@example.com");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> servico.finalizarCurso(aluno, -1.0)
        );

        assertEquals("Nota deve estar entre 0 e 10", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoParaNotaAcimaDoDez() {
        Aluno aluno = novoAluno(TipoAssinatura.ASSINATURA_PREMIUM, "nota.acima@example.com");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> servico.finalizarCurso(aluno, 10.1)
        );

        assertEquals("Nota deve estar entre 0 e 10", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoParaNotaMuitoAlta() {
        Aluno aluno = novoAluno(TipoAssinatura.ASSINATURA_BASICA, "nota.alta@example.com");

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> servico.finalizarCurso(aluno, 100.0)
        );

        assertEquals("Nota deve estar entre 0 e 10", exception.getMessage());
    }

    @Test
    void deveProcessarMultiplosCursosParaMesmoAluno() {
        Aluno aluno = novoAluno(TipoAssinatura.ASSINATURA_BASICA, "multiplos@example.com");
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 8.0);
        servico.finalizarCurso(aluno, 9.0);

        assertEquals(cursosIniciais + 6, aluno.getCursosDisponiveis());
    }

    @Test
    void deveProcessarMisturaDeCursosAprovadosEReprovados() {
        Aluno aluno = novoAluno(TipoAssinatura.ASSINATURA_PREMIUM, "mistura@example.com");
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 8.0);  // +3 cursos
        servico.finalizarCurso(aluno, 5.0);  // +0 cursos
        servico.finalizarCurso(aluno, 7.5);  // +3 cursos

        assertEquals(cursosIniciais + 6, aluno.getCursosDisponiveis());
    }

    @Test
    void deveVerificarQueAlunoSemprePodeReceberBonus() {
        Aluno alunoBasico = novoAluno(TipoAssinatura.ASSINATURA_BASICA, "bonus.basico@example.com");
        Aluno alunoPremium = novoAluno(TipoAssinatura.ASSINATURA_PREMIUM, "bonus.premium@example.com");
        int cursosBasicoIniciais = alunoBasico.getCursosDisponiveis();
        int cursosPremiumIniciais = alunoPremium.getCursosDisponiveis();

        servico.finalizarCurso(alunoBasico, 8.0);
        servico.finalizarCurso(alunoPremium, 8.0);

        assertTrue(alunoBasico.podeReceberBonus());
        assertTrue(alunoPremium.podeReceberBonus());
        assertEquals(cursosBasicoIniciais + 3, alunoBasico.getCursosDisponiveis());
        assertEquals(cursosPremiumIniciais + 3, alunoPremium.getCursosDisponiveis());
    }

    @Test
    void deveManterTipoAssinaturaAposFinalizarCurso() {
        Aluno aluno = novoAluno(TipoAssinatura.ASSINATURA_PREMIUM, "tipo.manter@example.com");
        TipoAssinatura tipoOriginal = aluno.getTipoAssinatura();

        servico.finalizarCurso(aluno, 8.5);

        assertEquals(tipoOriginal, aluno.getTipoAssinatura());
    }

    private Aluno novoAluno(TipoAssinatura tipoAssinatura, String email) {
        return Aluno.novo("Aluno " + tipoAssinatura.name(), ContatoEmail.of(email), tipoAssinatura);
    }
}
