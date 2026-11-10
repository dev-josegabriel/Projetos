package br.com.josedev.to_do_list.model;

public enum TaskStatus {
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em Andamento"),
    CONCLUIDA("Concluída");

    private String label;

    TaskStatus(String label) {
        this.label = label;
    }
}
