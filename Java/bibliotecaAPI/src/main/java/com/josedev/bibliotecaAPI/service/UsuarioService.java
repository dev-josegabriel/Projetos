package com.josedev.bibliotecaAPI.service;

import com.josedev.bibliotecaAPI.model.UsuarioEntity;
import com.josedev.bibliotecaAPI.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    // REGRA PARA CADASTRAR USUÁRIO: NÃO PODE TER EMAIL REPETIDO
    public UsuarioEntity cadastrarUsuario(UsuarioEntity usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("Email " + usuario.getEmail() + " já cadastrado");
        }
        return usuarioRepository.save(usuario);
    }

    // REGRA PARA LISTAR USUÁRIOS: LISTAR TODOS OS USUÁRIOS CADASTRADOS
    public List<UsuarioEntity> listAllUsers() {
        return usuarioRepository.findAll();
    }

    // REGRA PARA BUSCAR USUÁRIO POR ID: SE NÃO ENCONTRAR, LANÇAR EXCEÇÃO
    public UsuarioEntity getUserById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário de ID: " + id + ", não foi encontrado"));
    }

    // REGRA PARA DELETAR USUÁRIO POR ID
    public void deleteUserById(Long id) {
        getUserById(id); // valida existência e já lança a exceção customizada
        usuarioRepository.deleteById(id);
    }

}
