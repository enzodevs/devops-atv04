package br.com.facens.atividade4.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TipoAssinaturaTest {

    @Test
    void deveRetornarCursosIniciaisParaAssinaturaBasica() {
        assertEquals(5, TipoAssinatura.ASSINATURA_BASICA.getCursosIniciais());
    }

    @Test
    void deveRetornarCursosIniciaisParaAssinaturaPremium() {
        assertEquals(10, TipoAssinatura.ASSINATURA_PREMIUM.getCursosIniciais());
    }

    @Test
    void deveConverterStringValidaParaTipoAssinatura() {
        assertEquals(TipoAssinatura.ASSINATURA_BASICA,
                    TipoAssinatura.fromString("ASSINATURA_BASICA"));
        assertEquals(TipoAssinatura.ASSINATURA_PREMIUM,
                    TipoAssinatura.fromString("ASSINATURA_PREMIUM"));
    }

    @Test
    void deveLancarExcecaoParaStringInvalida() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TipoAssinatura.fromString("ASSINATURA_INEXISTENTE")
        );

        assertEquals("Tipo de assinatura inválido: ASSINATURA_INEXISTENTE", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoParaStringVazia() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TipoAssinatura.fromString("")
        );

        assertEquals("Tipo de assinatura inválido: ", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoParaStringNull() {
        NullPointerException exception = assertThrows(
            NullPointerException.class,
            () -> TipoAssinatura.fromString(null)
        );

        assertEquals("Name is null", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoParaStringComEspacos() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TipoAssinatura.fromString("ASSINATURA BASICA")
        );

        assertEquals("Tipo de assinatura inválido: ASSINATURA BASICA", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoParaStringMinuscula() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> TipoAssinatura.fromString("assinatura_basica")
        );

        assertEquals("Tipo de assinatura inválido: assinatura_basica", exception.getMessage());
    }

    @Test
    void deveVerificarTodosOsValoresDoEnum() {
        TipoAssinatura[] valores = TipoAssinatura.values();

        assertEquals(2, valores.length);
        assertEquals(TipoAssinatura.ASSINATURA_BASICA, valores[0]);
        assertEquals(TipoAssinatura.ASSINATURA_PREMIUM, valores[1]);
    }

    @Test
    void deveVerificarConsistenciaEntreEnumEFromString() {
        for (TipoAssinatura tipo : TipoAssinatura.values()) {
            assertEquals(tipo, TipoAssinatura.fromString(tipo.name()));
        }
    }
}