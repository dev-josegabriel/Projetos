package br.com.josedev.tarefas.controller;

import br.com.josedev.tarefas.dto.TarefaRequest;
import br.com.josedev.tarefas.dto.TarefasResponse;
import br.com.josedev.tarefas.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @GetMapping
    public TarefasResponse listar() {
        return service.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefasResponse adicionar(@Valid @RequestBody TarefaRequest request) {
        return service.adicionar(request);
    }

    @PutMapping("/{id}")
    public TarefasResponse editar(@PathVariable Long id, @Valid @RequestBody TarefaRequest request) {
        return service.editar(id, request);
    }

    @PatchMapping("/{id}/concluir")
    public TarefasResponse alternarConcluida(@PathVariable Long id) {
        return service.alternarConcluida(id);
    }

    @DeleteMapping("/{id}")
    public TarefasResponse remover(@PathVariable Long id) {
        return service.remover(id);
    }

    @DeleteMapping("/concluidas")
    public TarefasResponse limparConcluidas() {
        return service.limparConcluidas();
    }
}
