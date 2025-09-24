package br.com.facens.atividade4.domain;

/**
 * Representa um aluno no sistema de cursos.
 * Cada aluno possui um tipo de assinatura e um número de cursos disponíveis.
 */
public class Aluno {
    private static final String ERRO_QUANTIDADE_NEGATIVA = "Quantidade de cursos não pode ser negativa";

    private final TipoAssinatura tipoAssinatura;
    private int cursosDisponiveis;

    /**
     * Constrói um aluno a partir de uma string representando o tipo de assinatura.
     *
     * @param tipoAssinatura string representando o tipo de assinatura
     */
    public Aluno(String tipoAssinatura) {
        this(TipoAssinatura.fromString(tipoAssinatura));
    }

    /**
     * Constrói um aluno com um tipo de assinatura específico.
     *
     * @param tipoAssinatura o tipo de assinatura do aluno
     */
    public Aluno(TipoAssinatura tipoAssinatura) {
        this.tipoAssinatura = tipoAssinatura;
        inicializarCursosDisponiveis();
    }

    public int getCursosDisponiveis() {
        return cursosDisponiveis;
    }

    /**
     * Adiciona uma quantidade de cursos aos cursos disponíveis do aluno.
     *
     * @param quantidade a quantidade de cursos a ser adicionada (não pode ser negativa)
     * @throws IllegalArgumentException se a quantidade for negativa
     */
    public void adicionarCursos(int quantidade) {
        validarQuantidadeCursos(quantidade);
        incrementarCursosDisponiveis(quantidade);
    }

    public TipoAssinatura getTipoAssinatura() {
        return tipoAssinatura;
    }

    /**
     * Verifica se o aluno pode receber bônus de cursos.
     *
     * @return sempre true, pois todos os alunos podem receber bônus
     */
    public boolean podeReceberBonus() {
        return true;
    }

    /**
     * Inicializa a quantidade de cursos disponíveis baseada no tipo de assinatura.
     */
    private void inicializarCursosDisponiveis() {
        this.cursosDisponiveis = this.tipoAssinatura.getCursosIniciais();
    }

    /**
     * Valida se a quantidade de cursos é válida (não negativa).
     */
    private void validarQuantidadeCursos(int quantidade) {
        if (isQuantidadeNegativa(quantidade)) {
            throw new IllegalArgumentException(ERRO_QUANTIDADE_NEGATIVA);
        }
    }

    /**
     * Verifica se a quantidade é negativa.
     */
    private boolean isQuantidadeNegativa(int quantidade) {
        return quantidade < 0;
    }

    /**
     * Incrementa a quantidade de cursos disponíveis.
     */
    private void incrementarCursosDisponiveis(int quantidade) {
        this.cursosDisponiveis += quantidade;
    }
}