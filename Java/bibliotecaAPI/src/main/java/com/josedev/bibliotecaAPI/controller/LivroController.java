package com.josedev.bibliotecaAPI.controller;

import com.josedev.bibliotecaAPI.model.LivroEntity;
import com.josedev.bibliotecaAPI.service.LivroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/livro")
@RequiredArgsConstructor
public class LivroController {
    private final LivroService livroService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<LivroEntity>> listAllBooks() {
        List<LivroEntity> livros = livroService.listAllBooks();
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<LivroEntity> listBooksById(@PathVariable Long id) {
        LivroEntity livro = livroService.listBooksById(id);
        return ResponseEntity.ok(livro);
    }

    @PostMapping
    public ResponseEntity<LivroEntity> cadastrarLivro(@RequestBody LivroEntity livro) {
        LivroEntity livroSalvo = livroService.cadastrarLivro(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLivroPorId(@PathVariable Long id) {
        livroService.deleteBookById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
