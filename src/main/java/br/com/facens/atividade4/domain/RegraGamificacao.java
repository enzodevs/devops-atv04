package br.com.facens.atividade4.domain;

/**
 * Classe responsável pelas regras de gamificação do sistema de cursos.
 * Define critérios de aprovação e cálculo de bônus para alunos.
 */
public class RegraGamificacao {
    private static final double NOTA_MINIMA_APROVACAO = 7.0;
    private static final int CURSOS_BONUS_APROVACAO = 3;
    private static final int CURSOS_BONUS_REPROVACAO = 0;

    /**
     * Verifica se um aluno foi aprovado baseado em sua nota.
     *
     * @param nota a nota do aluno (deve estar entre 0.0 e 10.0)
     * @return true se a nota for maior ou igual à nota mínima para aprovação
     */
    public static boolean aprovado(double nota) {
        return isNotaMaiorOuIgualAoMinimo(nota);
    }

    /**
     * Calcula a quantidade de cursos bônus que um aluno deve receber.
     *
     * @param nota a nota do aluno
     * @return quantidade de cursos bônus (3 se aprovado, 0 se reprovado)
     */
    public static int calcularCursosBonus(double nota) {
        return aprovado(nota) ? CURSOS_BONUS_APROVACAO : CURSOS_BONUS_REPROVACAO;
    }

    /**
     * Método auxiliar para verificar se a nota atende ao critério mínimo.
     */
    private static boolean isNotaMaiorOuIgualAoMinimo(double nota) {
        return nota >= NOTA_MINIMA_APROVACAO;
    }
}