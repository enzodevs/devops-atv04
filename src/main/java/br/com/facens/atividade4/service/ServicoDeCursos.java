package br.com.facens.atividade4.service;

import org.springframework.stereotype.Service;

import br.com.facens.atividade4.domain.Aluno;
import br.com.facens.atividade4.domain.RegraGamificacao;

/**
 * Serviço responsável pelas regras de gamificação aplicadas aos cursos.
 */
@Service
public class ServicoDeCursos {
    private static final double NOTA_MINIMA = 0.0;
    private static final double NOTA_MAXIMA = 10.0;
    private static final String ERRO_ALUNO_NULO = "Aluno não pode ser nulo";
    private static final String ERRO_NOTA_INVALIDA = "Nota deve estar entre 0 e 10";

    /**
     * Finaliza um curso para um aluno, aplicando as regras de gamificação se aplicável.
     *
     * @param aluno o aluno que finalizou o curso
     * @param nota  a nota obtida pelo aluno no curso
     * @throws IllegalArgumentException se os parâmetros forem inválidos
     */
    public void finalizarCurso(Aluno aluno, double nota) {
        validarParametros(aluno, nota);

        if (isElegivelParaBonusPorAprovacao(aluno, nota)) {
            aplicarBonusPorAprovacao(aluno, nota);
        }
    }

    /**
     * Valida os parâmetros de entrada para finalização do curso.
     */
    private void validarParametros(Aluno aluno, double nota) {
        validarAluno(aluno);
        validarNota(nota);
    }

    /**
     * Valida se o aluno não é nulo.
     */
    private void validarAluno(Aluno aluno) {
        if (isAlunoNulo(aluno)) {
            throw new IllegalArgumentException(ERRO_ALUNO_NULO);
        }
    }

    /**
     * Valida se a nota está dentro do intervalo válido.
     */
    private void validarNota(double nota) {
        if (isNotaInvalida(nota)) {
            throw new IllegalArgumentException(ERRO_NOTA_INVALIDA);
        }
    }

    /**
     * Verifica se o aluno é elegível para receber bônus por aprovação.
     */
    private boolean isElegivelParaBonusPorAprovacao(Aluno aluno, double nota) {
        return aluno.podeReceberBonus() && RegraGamificacao.aprovado(nota);
    }

    /**
     * Aplica o bônus de cursos por aprovação.
     */
    private void aplicarBonusPorAprovacao(Aluno aluno, double nota) {
        int cursosBonus = RegraGamificacao.calcularCursosBonus(nota);
        aluno.adicionarCursos(cursosBonus);
    }

    /**
     * Verifica se o aluno é nulo.
     */
    private boolean isAlunoNulo(Aluno aluno) {
        return aluno == null;
    }

    /**
     * Verifica se a nota está fora do intervalo válido.
     */
    private boolean isNotaInvalida(double nota) {
        return nota < NOTA_MINIMA || nota > NOTA_MAXIMA;
    }
}
