package br.com.facens.atividade4.service;

import br.com.facens.atividade4.domain.Aluno;
import br.com.facens.atividade4.domain.TipoAssinatura;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class ServicoDeCursosTest {

    private ServicoDeCursos servico;

    @BeforeEach
    void setUp() {
        servico = new ServicoDeCursos();
    }

    @Test
    void deveAdicionarCursosBonusParaAlunoAprovado() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_BASICA);
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 8.0);

        assertEquals(cursosIniciais + 3, aluno.getCursosDisponiveis());
    }

    @Test
    void deveAdicionarCursosBonusParaNotaLimite() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_PREMIUM);
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 7.0);

        assertEquals(cursosIniciais + 3, aluno.getCursosDisponiveis());
    }

    @Test
    void naoDeveAdicionarCursosBonusParaAlunoReprovado() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_BASICA);
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 6.9);

        assertEquals(cursosIniciais, aluno.getCursosDisponiveis());
    }

    @Test
    void naoDeveAdicionarCursosBonusParaNotaZero() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_PREMIUM);
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 0.0);

        assertEquals(cursosIniciais, aluno.getCursosDisponiveis());
    }

    @Test
    void deveAdicionarCursosBonusParaNotaMaxima() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_BASICA);
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
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_BASICA);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> servico.finalizarCurso(aluno, -1.0)
        );

        assertEquals("Nota deve estar entre 0 e 10", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoParaNotaAcimaDoDez() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_PREMIUM);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> servico.finalizarCurso(aluno, 10.1)
        );

        assertEquals("Nota deve estar entre 0 e 10", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoParaNotaMuitoAlta() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_BASICA);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> servico.finalizarCurso(aluno, 100.0)
        );

        assertEquals("Nota deve estar entre 0 e 10", exception.getMessage());
    }

    @Test
    void deveProcessarMultiplosCursosParaMesmoAluno() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_BASICA);
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 8.0);
        servico.finalizarCurso(aluno, 9.0);

        assertEquals(cursosIniciais + 6, aluno.getCursosDisponiveis());
    }

    @Test
    void deveProcessarMisturaDeCursosAprovadosEReprovados() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_PREMIUM);
        int cursosIniciais = aluno.getCursosDisponiveis();

        servico.finalizarCurso(aluno, 8.0);  // +3 cursos
        servico.finalizarCurso(aluno, 5.0);  // +0 cursos
        servico.finalizarCurso(aluno, 7.5);  // +3 cursos

        assertEquals(cursosIniciais + 6, aluno.getCursosDisponiveis());
    }

    @Test
    void deveVerificarQueAlunoSemprePodeReceberBonus() {
        Aluno alunoBasico = new Aluno(TipoAssinatura.ASSINATURA_BASICA);
        Aluno alunoPremium = new Aluno(TipoAssinatura.ASSINATURA_PREMIUM);
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
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_PREMIUM);
        TipoAssinatura tipoOriginal = aluno.getTipoAssinatura();

        servico.finalizarCurso(aluno, 8.5);

        assertEquals(tipoOriginal, aluno.getTipoAssinatura());
    }
}