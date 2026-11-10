package com.josedev.bibliotecaAPI.repository;

import com.josedev.bibliotecaAPI.model.EmprestimoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmprestimoRepository extends JpaRepository<EmprestimoEntity, Long> {

}
