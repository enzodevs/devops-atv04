package br.com.facens.atividade4.domain;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Representa um aluno no sistema de cursos.
 * Cada aluno possui identidade, contato e assinatura que definem seu plano.
 */
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_aluno")
public class Aluno {
    private static final String ERRO_QUANTIDADE_NEGATIVA = "Quantidade de cursos não pode ser negativa";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Embedded
    private ContatoEmail contatoEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoAssinatura tipoAssinatura;

    @Column(nullable = false)
    private int cursosDisponiveis;

    @Builder
    private Aluno(Long id,
                  String nome,
                  ContatoEmail contatoEmail,
                  TipoAssinatura tipoAssinatura,
                  Integer cursosDisponiveis) {
        this.id = id;
        this.nome = validarNome(nome);
        this.contatoEmail = Objects.requireNonNull(contatoEmail, "Contato do aluno é obrigatório");
        this.tipoAssinatura = Objects.requireNonNull(tipoAssinatura, "Tipo de assinatura é obrigatório");
        this.cursosDisponiveis = cursosDisponiveis != null
                ? validarCursosNaoNegativos(cursosDisponiveis)
                : this.tipoAssinatura.getCursosIniciais();
    }

    public static Aluno novo(String nome, ContatoEmail contatoEmail, TipoAssinatura tipoAssinatura) {
        return Aluno.builder()
                .nome(nome)
                .contatoEmail(contatoEmail)
                .tipoAssinatura(tipoAssinatura)
                .build();
    }

    public void atualizarDados(String nome, ContatoEmail contatoEmail, TipoAssinatura tipoAssinatura) {
        this.nome = validarNome(nome);
        this.contatoEmail = Objects.requireNonNull(contatoEmail, "Contato do aluno é obrigatório");
        this.tipoAssinatura = Objects.requireNonNull(tipoAssinatura, "Tipo de assinatura é obrigatório");
    }

    public void adicionarCursos(int quantidade) {
        validarQuantidadeCursos(quantidade);
        incrementarCursosDisponiveis(quantidade);
    }

    public boolean podeReceberBonus() {
        return true;
    }

    private static String validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do aluno é obrigatório");
        }
        return nome;
    }

    private void validarQuantidadeCursos(int quantidade) {
        if (isQuantidadeNegativa(quantidade)) {
            throw new IllegalArgumentException(ERRO_QUANTIDADE_NEGATIVA);
        }
    }

    private boolean isQuantidadeNegativa(int quantidade) {
        return quantidade < 0;
    }

    private int validarCursosNaoNegativos(int quantidade) {
        if (quantidade < 0) {
            throw new IllegalArgumentException(ERRO_QUANTIDADE_NEGATIVA);
        }
        return quantidade;
    }

    private void incrementarCursosDisponiveis(int quantidade) {
        this.cursosDisponiveis += quantidade;
    }
}
