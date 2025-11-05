package br.com.facens.atividade4.domain;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Value Object que representa e valida o endereço de e-mail de um aluno.
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class ContatoEmail {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Column(name = "email", nullable = false, length = 120, unique = true)
    private String endereco;

    private ContatoEmail(String endereco) {
        this.endereco = endereco;
    }

    public static ContatoEmail of(String endereco) {
        String emailValidado = validar(endereco);
        return new ContatoEmail(emailValidado);
    }

    private static String validar(String valor) {
        String email = Objects.requireNonNull(valor, "Email é obrigatório").trim();
        if (email.isEmpty() || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email inválido");
        }
        return email.toLowerCase(Locale.ROOT);
    }

    @Override
    public String toString() {
        return endereco;
    }
}
