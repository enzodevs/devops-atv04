package br.com.facens.atividade4.domain;

public enum TipoAssinatura {
    ASSINATURA_BASICA(5),
    ASSINATURA_PREMIUM(10);

    private final int cursosIniciais;

    TipoAssinatura(int cursosIniciais) {
        this.cursosIniciais = cursosIniciais;
    }

    public int getCursosIniciais() {
        return cursosIniciais;
    }

    public static TipoAssinatura fromString(String tipo) {
        try {
            return TipoAssinatura.valueOf(tipo);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de assinatura inválido: " + tipo, e);
        }
    }
}
