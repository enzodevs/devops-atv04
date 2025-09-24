package br.com.facens.atividade4;

public class Aluno {
    private String tipoAssinatura;
    private int cursosDisponiveis;

    public Aluno(String tipoAssinatura) {
        this.tipoAssinatura = tipoAssinatura;
        this.cursosDisponiveis = "ASSINATURA_BASICA".equals(tipoAssinatura) ? 5 : 10;
    }

    public int getCursosDisponiveis() {
        return cursosDisponiveis;
    }

    public void adicionarCursos(int quantidade) {
        this.cursosDisponiveis += quantidade;
    }

    public String getTipoAssinatura() {
        return tipoAssinatura;
    }
}
