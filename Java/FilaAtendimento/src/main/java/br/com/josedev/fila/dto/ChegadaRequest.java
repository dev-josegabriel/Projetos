package br.com.josedev.fila.dto;

import br.com.josedev.fila.model.Prioridade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Dados que chegam do front quando alguém entra na fila. */
public record ChegadaRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 60, message = "O nome pode ter no máximo 60 caracteres.") String nome,
        @NotNull(message = "Escolha a prioridade.") Prioridade prioridade
) {
}
