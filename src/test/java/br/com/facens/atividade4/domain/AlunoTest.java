package br.com.facens.atividade4.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlunoTest {

    @Test
    void deveCriarAlunoComAssinaturaBasica() {
        Aluno aluno = new Aluno("ASSINATURA_BASICA");

        assertEquals(TipoAssinatura.ASSINATURA_BASICA, aluno.getTipoAssinatura());
        assertEquals(5, aluno.getCursosDisponiveis());
    }

    @Test
    void deveCriarAlunoComAssinaturaPremium() {
        Aluno aluno = new Aluno("ASSINATURA_PREMIUM");

        assertEquals(TipoAssinatura.ASSINATURA_PREMIUM, aluno.getTipoAssinatura());
        assertEquals(10, aluno.getCursosDisponiveis());
    }

    @Test
    void deveCriarAlunoComTipoAssinaturaEnum() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_BASICA);

        assertEquals(TipoAssinatura.ASSINATURA_BASICA, aluno.getTipoAssinatura());
        assertEquals(5, aluno.getCursosDisponiveis());
    }

    @Test
    void deveLancarExcecaoParaTipoAssinaturaInvalido() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new Aluno("ASSINATURA_INEXISTENTE")
        );

        assertEquals("Tipo de assinatura inválido: ASSINATURA_INEXISTENTE", exception.getMessage());
    }

    @Test
    void deveAdicionarCursosCorretamente() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_BASICA);
        int cursosIniciais = aluno.getCursosDisponiveis();

        aluno.adicionarCursos(3);

        assertEquals(cursosIniciais + 3, aluno.getCursosDisponiveis());
    }

    @Test
    void deveAdicionarZeroCursos() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_BASICA);
        int cursosIniciais = aluno.getCursosDisponiveis();

        aluno.adicionarCursos(0);

        assertEquals(cursosIniciais, aluno.getCursosDisponiveis());
    }

    @Test
    void deveAdicionarMultiplasChamadas() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_BASICA);
        int cursosIniciais = aluno.getCursosDisponiveis();

        aluno.adicionarCursos(2);
        aluno.adicionarCursos(3);
        aluno.adicionarCursos(1);

        assertEquals(cursosIniciais + 6, aluno.getCursosDisponiveis());
    }

    @Test
    void deveLancarExcecaoParaQuantidadeNegativaDeCursos() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_BASICA);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> aluno.adicionarCursos(-1)
        );

        assertEquals("Quantidade de cursos não pode ser negativa", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoParaQuantidadeNegativaGrande() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_BASICA);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> aluno.adicionarCursos(-100)
        );

        assertEquals("Quantidade de cursos não pode ser negativa", exception.getMessage());
    }

    @Test
    void deveSemprePoderReceberBonus() {
        Aluno alunoBasico = new Aluno(TipoAssinatura.ASSINATURA_BASICA);
        Aluno alunoPremium = new Aluno(TipoAssinatura.ASSINATURA_PREMIUM);

        assertTrue(alunoBasico.podeReceberBonus());
        assertTrue(alunoPremium.podeReceberBonus());
    }

    @Test
    void deveManterTipoAssinaturaAposAdicionarCursos() {
        Aluno aluno = new Aluno(TipoAssinatura.ASSINATURA_PREMIUM);
        TipoAssinatura tipoOriginal = aluno.getTipoAssinatura();

        aluno.adicionarCursos(5);

        assertEquals(tipoOriginal, aluno.getTipoAssinatura());
    }
}