package com.josedev.bibliotecaAPI.controller;

import com.josedev.bibliotecaAPI.model.EmprestimoEntity;
import com.josedev.bibliotecaAPI.service.EmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/emprestimo")
@RequiredArgsConstructor
public class EmprestimoController {
    private final EmprestimoService emprestimoService;

    @PostMapping
    public ResponseEntity<EmprestimoEntity> criarEmprestimo(@RequestParam Long livroId, @RequestParam Long usuarioId) {
        EmprestimoEntity emprestimoSalvo = emprestimoService.criarEmprestimo(livroId, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoSalvo);
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<EmprestimoEntity> devolverLivro(@PathVariable Long id) {
        EmprestimoEntity emprestimoDevolvido = emprestimoService.devolverLivro(id);
        return ResponseEntity.ok(emprestimoDevolvido);
    }
}