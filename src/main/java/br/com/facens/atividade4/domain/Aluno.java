package br.com.facens.atividade4.domain;

public class Aluno {
    private final TipoAssinatura tipoAssinatura;
    private int cursosDisponiveis;

    public Aluno(String tipoAssinatura) {
        this.tipoAssinatura = TipoAssinatura.fromString(tipoAssinatura);
        this.cursosDisponiveis = this.tipoAssinatura.getCursosIniciais();
    }

    public Aluno(TipoAssinatura tipoAssinatura) {
        this.tipoAssinatura = tipoAssinatura;
        this.cursosDisponiveis = tipoAssinatura.getCursosIniciais();
    }

    public int getCursosDisponiveis() {
        return cursosDisponiveis;
    }

    public void adicionarCursos(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade de cursos não pode ser negativa");
        }
        this.cursosDisponiveis += quantidade;
    }

    public TipoAssinatura getTipoAssinatura() {
        return tipoAssinatura;
    }

    public boolean podeReceberBonus() {
        return true; // Regra de negócio: todos os alunos podem receber bônus
    }
}