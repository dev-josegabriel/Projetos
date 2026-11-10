package br.com.josedev.fila.dto;

import br.com.josedev.fila.model.Atendimento;

import java.util.List;

/** Fila na ordem em que as pessoas serão chamadas, com os contadores já calculados. */
public record FilaResponse(
        List<Atendimento> fila,
        int total,
        int emergencias,
        int prioritarios,
        int normais,
        int atendidos,
        Atendimento chamado
) {
}
