package br.com.facens.atividade4.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AlunoTest {

    @Test
    void deveCriarAlunoComAssinaturaBasica() {
        Aluno aluno = Aluno.novo("Ana Cursos", ContatoEmail.of("ana.cursos@example.com"),
                TipoAssinatura.ASSINATURA_BASICA);

        assertEquals(TipoAssinatura.ASSINATURA_BASICA, aluno.getTipoAssinatura());
        assertEquals(5, aluno.getCursosDisponiveis());
    }

    @Test
    void deveCriarAlunoComAssinaturaPremium() {
        Aluno aluno = Aluno.novo("Paulo Avancado", ContatoEmail.of("paulo.avancado@example.com"),
                TipoAssinatura.ASSINATURA_PREMIUM);

        assertEquals(TipoAssinatura.ASSINATURA_PREMIUM, aluno.getTipoAssinatura());
        assertEquals(10, aluno.getCursosDisponiveis());
    }

    @Test
    void deveAtualizarDadosDoAluno() {
        Aluno aluno = Aluno.novo("Aluno Original", ContatoEmail.of("original@example.com"),
                TipoAssinatura.ASSINATURA_BASICA);

        aluno.atualizarDados("Aluno Atualizado", ContatoEmail.of("atualizado@example.com"),
                TipoAssinatura.ASSINATURA_PREMIUM);

        assertEquals("Aluno Atualizado", aluno.getNome());
        assertEquals("atualizado@example.com", aluno.getContatoEmail().getEndereco());
        assertEquals(TipoAssinatura.ASSINATURA_PREMIUM, aluno.getTipoAssinatura());
    }

    @Test
    void deveAdicionarCursosCorretamente() {
        Aluno aluno = Aluno.novo("Aluno Cursos", ContatoEmail.of("cursos@example.com"),
                TipoAssinatura.ASSINATURA_BASICA);
        int cursosIniciais = aluno.getCursosDisponiveis();

        aluno.adicionarCursos(3);

        assertEquals(cursosIniciais + 3, aluno.getCursosDisponiveis());
    }

    @Test
    void deveLancarExcecaoParaQuantidadeNegativaDeCursos() {
        Aluno aluno = Aluno.novo("Aluno Negativo", ContatoEmail.of("negativo@example.com"),
                TipoAssinatura.ASSINATURA_BASICA);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> aluno.adicionarCursos(-1)
        );

        assertEquals("Quantidade de cursos não pode ser negativa", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoCursosIniciaisNegativos() {
        assertThrows(IllegalArgumentException.class, () -> Aluno.builder()
                .nome("Aluno Invalido")
                .contatoEmail(ContatoEmail.of("invalido@example.com"))
                .tipoAssinatura(TipoAssinatura.ASSINATURA_BASICA)
                .cursosDisponiveis(-5)
                .build());
    }

    @Test
    void deveSemprePoderReceberBonus() {
        Aluno alunoBasico = Aluno.novo("Aluno Basico", ContatoEmail.of("basico@example.com"),
                TipoAssinatura.ASSINATURA_BASICA);
        Aluno alunoPremium = Aluno.novo("Aluno Premium", ContatoEmail.of("premium@example.com"),
                TipoAssinatura.ASSINATURA_PREMIUM);

        assertTrue(alunoBasico.podeReceberBonus());
        assertTrue(alunoPremium.podeReceberBonus());
    }

    @Test
    void deveCriarAlunoComCursosPersonalizados() {
        Aluno aluno = Aluno.builder()
                .id(99L)
                .nome("Aluno Personalizado")
                .contatoEmail(ContatoEmail.of("personalizado@example.com"))
                .tipoAssinatura(TipoAssinatura.ASSINATURA_PREMIUM)
                .cursosDisponiveis(15)
                .build();

        assertEquals(99L, aluno.getId());
        assertEquals(15, aluno.getCursosDisponiveis());
        assertEquals("Aluno Personalizado", aluno.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoNomeForInvalido() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Aluno.builder()
                        .nome("   ")
                        .contatoEmail(ContatoEmail.of("nome.invalido@example.com"))
                        .tipoAssinatura(TipoAssinatura.ASSINATURA_BASICA)
                        .build());

        assertEquals("Nome do aluno é obrigatório", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Aluno.builder()
                        .nome(null)
                        .contatoEmail(ContatoEmail.of("nome.nulo@example.com"))
                        .tipoAssinatura(TipoAssinatura.ASSINATURA_BASICA)
                        .build());

        assertEquals("Nome do aluno é obrigatório", exception.getMessage());
    }

}
