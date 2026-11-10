package com.josedev.bibliotecaAPI.repository;

import com.josedev.bibliotecaAPI.model.LivroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LivroRepository extends JpaRepository<LivroEntity, Long> {
    Optional<LivroEntity> findByIsbn(String isbn);

}
