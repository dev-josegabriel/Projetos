package br.com.josedev.fila.model;

/** Pessoa que já foi chamada, com o horário da chamada. */
public record Chamado(int id, String senha, String nome, Prioridade prioridade, String chegada, String chamadoEm) {
}
