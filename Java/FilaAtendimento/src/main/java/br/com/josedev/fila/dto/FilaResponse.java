package br.com.josedev.fila.dto;

import br.com.josedev.fila.model.Atendimento;

import java.util.List;

/** Fila na ordem de chamada, o vetor do heap (para desenhar a árvore) e os contadores já calculados. */
public record FilaResponse(
        List<Atendimento> fila,
        List<Atendimento> heap,
        int total,
        int emergencias,
        int prioritarios,
        int normais,
        int atendidos,
        Atendimento chamado
) {
}
