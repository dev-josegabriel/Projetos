package br.com.josedev.fila.model;

/** Quanto menor o nível, mais cedo a pessoa é chamada. */
public enum Prioridade {
    EMERGENCIA(1),
    PRIORITARIO(2),
    NORMAL(3);

    private final int nivel;

    Prioridade(int nivel) {
        this.nivel = nivel;
    }

    public int getNivel() {
        return nivel;
    }
}
