package com.josedev.bibliotecaAPI.service;

import com.josedev.bibliotecaAPI.model.LivroEntity;
import com.josedev.bibliotecaAPI.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository livroRepository;

    public LivroEntity cadastrarLivro(LivroEntity livro) {
        if (livroRepository.findByIsbn(livro.getIsbn()).isPresent()) {
            throw new RuntimeException("Livro com ISBN " + livro.getIsbn() + " já cadastrado");
        }
        return livroRepository.save(livro);
    }

    public List<LivroEntity> listAllBooks() {
        return livroRepository.findAll();
    }

    public LivroEntity listBooksById(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livro de ID: " + id + ", não foi encontrado"));
    }

    public void deleteBookById(Long id) {
        listBooksById(id); // valida existência e já lança a exceção customizada
        livroRepository.deleteById(id);
    }
}
