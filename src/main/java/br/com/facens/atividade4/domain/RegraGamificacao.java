package br.com.facens.atividade4.domain;

public class RegraGamificacao {
    private static final double NOTA_MINIMA_APROVACAO = 7.0;
    private static final int CURSOS_BONUS_APROVACAO = 3;

    public static boolean aprovado(double nota) {
        return nota >= NOTA_MINIMA_APROVACAO;
    }

    public static int calcularCursosBonus(double nota) {
        return aprovado(nota) ? CURSOS_BONUS_APROVACAO : 0;
    }
}