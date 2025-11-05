package br.com.facens.atividade4.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ContatoEmailTest {

    @Test
    void deveCriarContatoEmailValido() {
        ContatoEmail contatoEmail = ContatoEmail.of("Usuario.Exemplo@Email.Com");

        assertEquals("usuario.exemplo@email.com", contatoEmail.getEndereco());
        assertEquals("usuario.exemplo@email.com", contatoEmail.toString());
    }

    @Test
    void deveLancarExcecaoQuandoEmailForInvalido() {
        assertThrows(IllegalArgumentException.class, () -> ContatoEmail.of("invalido-sem-arroba"));
    }

    @Test
    void deveLancarExcecaoQuandoEmailForNulo() {
        assertThrows(NullPointerException.class, () -> ContatoEmail.of(null));
    }

    @Test
    void deveLancarExcecaoQuandoEmailForVazio() {
        assertThrows(IllegalArgumentException.class, () -> ContatoEmail.of(""));
    }

    @Test
    void deveLancarExcecaoQuandoEmailForApenasEspacos() {
        assertThrows(IllegalArgumentException.class, () -> ContatoEmail.of("   "));
    }
}
