package br.com.josedev.tarefas.dto;

import br.com.josedev.tarefas.model.Tarefa;

import java.util.List;

/** Estado completo da lista, do início ao fim, mais os contadores. */
public record TarefasResponse(
        List<Tarefa> tarefas,
        int total,
        int concluidas,
        int pendentes
) {
}
