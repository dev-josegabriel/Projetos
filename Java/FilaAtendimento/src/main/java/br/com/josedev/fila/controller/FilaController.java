package br.com.josedev.fila.controller;

import br.com.josedev.fila.dto.ChegadaRequest;
import br.com.josedev.fila.dto.FilaResponse;
import br.com.josedev.fila.service.FilaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fila")
public class FilaController {

    private final FilaService service;

    public FilaController(FilaService service) {
        this.service = service;
    }

    @GetMapping
    public FilaResponse listar() {
        return service.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilaResponse chegar(@Valid @RequestBody ChegadaRequest request) {
        return service.chegar(request.nome(), request.prioridade());
    }

    @PostMapping("/chamar")
    public FilaResponse chamarProximo() {
        return service.chamarProximo();
    }

    @DeleteMapping("/{id}")
    public FilaResponse cancelar(@PathVariable int id) {
        return service.cancelar(id);
    }
}
