package br.com.facens.atividade4.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegraGamificacaoTest {

    @Test
    void deveAprovarAlunoComNotaAcimaDaMinima() {
        assertTrue(RegraGamificacao.aprovado(7.5));
        assertTrue(RegraGamificacao.aprovado(8.0));
        assertTrue(RegraGamificacao.aprovado(10.0));
    }

    @Test
    void deveAprovarAlunoComNotaExatamenteIgualAMinima() {
        assertTrue(RegraGamificacao.aprovado(7.0));
    }

    @Test
    void deveReprovarAlunoComNotaAbaixoDaMinima() {
        assertFalse(RegraGamificacao.aprovado(6.9));
        assertFalse(RegraGamificacao.aprovado(5.0));
        assertFalse(RegraGamificacao.aprovado(0.0));
    }

    @Test
    void deveCalcularCursosBonusParaAlunoAprovado() {
        assertEquals(3, RegraGamificacao.calcularCursosBonus(7.0));
        assertEquals(3, RegraGamificacao.calcularCursosBonus(8.5));
        assertEquals(3, RegraGamificacao.calcularCursosBonus(10.0));
    }

    @Test
    void deveCalcularZeroCursosBonusParaAlunoReprovado() {
        assertEquals(0, RegraGamificacao.calcularCursosBonus(6.9));
        assertEquals(0, RegraGamificacao.calcularCursosBonus(5.0));
        assertEquals(0, RegraGamificacao.calcularCursosBonus(0.0));
    }

    @Test
    void deveTestarConsistenciaEntreMetodos() {
        double[] notas = {0.0, 3.5, 6.9, 7.0, 7.1, 8.5, 10.0};

        for (double nota : notas) {
            boolean aprovado = RegraGamificacao.aprovado(nota);
            int cursosBonus = RegraGamificacao.calcularCursosBonus(nota);

            if (aprovado) {
                assertEquals(3, cursosBonus, "Aluno aprovado deve receber 3 cursos bônus");
            } else {
                assertEquals(0, cursosBonus, "Aluno reprovado deve receber 0 cursos bônus");
            }
        }
    }
}