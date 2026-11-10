package com.josedev.bibliotecaAPI.service;

import com.josedev.bibliotecaAPI.model.EmprestimoEntity;
import com.josedev.bibliotecaAPI.model.LivroEntity;
import com.josedev.bibliotecaAPI.model.UsuarioEntity;
import com.josedev.bibliotecaAPI.repository.EmprestimoRepository;
import com.josedev.bibliotecaAPI.repository.LivroRepository;
import com.josedev.bibliotecaAPI.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    public EmprestimoEntity criarEmprestimo(Long livroId, Long usuarioId) {
        LivroEntity livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!livro.isDisponivel()) {
            throw new RuntimeException("Livro não está disponível para empréstimo");
        }

        livro.setDisponivel(false);
        livroRepository.save(livro);

        EmprestimoEntity emprestimo = new EmprestimoEntity();
        emprestimo.setLivro(livro);
        emprestimo.setUsuario(usuario);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataPrevistaDevolucao(LocalDate.now().plusDays(7));

        return emprestimoRepository.save(emprestimo);
    }

    public EmprestimoEntity devolverLivro(Long emprestimoId) {
        EmprestimoEntity emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new RuntimeException("Empréstimo não econtrado"));
        if (emprestimo.getDataDevolucaoEfetiva() != null) {
            throw new RuntimeException("Esse empréstimo já foi devolvido");
        }

        emprestimo.setDataDevolucaoEfetiva(LocalDate.now());

        LivroEntity livro = emprestimo.getLivro();
        livro.setDisponivel(true);
        livroRepository.save(livro);

        return emprestimoRepository.save(emprestimo);
    }
}
