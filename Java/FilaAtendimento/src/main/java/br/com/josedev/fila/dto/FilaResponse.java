package br.com.josedev.fila.dto;

import br.com.josedev.fila.model.Atendimento;
import br.com.josedev.fila.model.Chamado;

import java.util.List;

/** Fila na ordem de chamada, todos os já chamados (o mais recente primeiro) e os contadores já calculados. */
public record FilaResponse(
        List<Atendimento> fila,
        List<Chamado> historico,
        int total,
        int emergencias,
        int prioritarios,
        int normais,
        int atendidos,
        Atendimento chamado
) {
}
