package br.com.josedev.fila.model;

/** Uma pessoa esperando (ou já chamada) na fila de atendimento. */
public record Atendimento(int id, String senha, String nome, Prioridade prioridade, String chegada) {
}
