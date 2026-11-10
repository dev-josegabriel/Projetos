package br.com.josedev.tarefas.model;

public class Tarefa {

    private final Long id;
    private String titulo;
    private boolean concluida;

    public Tarefa(Long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
        this.concluida = false;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }
}
