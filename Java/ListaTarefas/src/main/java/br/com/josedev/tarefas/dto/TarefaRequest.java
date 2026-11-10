package br.com.josedev.tarefas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Dados que chegam do front.
 * "posicao" é opcional: se vier vazio, a tarefa entra no fim da lista.
 * (No editar, a posição é ignorada.)
 */
public record TarefaRequest(
        @NotBlank(message = "O título é obrigatório.")
        @Size(max = 100, message = "O título pode ter no máximo 100 caracteres.") String titulo,
        Integer posicao
) {
}
