package com.josedev.bibliotecaAPI.controller;

import com.josedev.bibliotecaAPI.model.UsuarioEntity;
import com.josedev.bibliotecaAPI.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UsuarioEntity>> listAllUsers() {
        List<UsuarioEntity> usuarios = usuarioService.listAllUsers();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UsuarioEntity> getUserById(@PathVariable Long id) {
        UsuarioEntity usuario = usuarioService.getUserById(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UsuarioEntity> cadastrarUsuario(@RequestBody UsuarioEntity usuario) {
        UsuarioEntity usuarioSalvo = usuarioService.cadastrarUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletarUsuarioById(@PathVariable Long id) {
        usuarioService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
