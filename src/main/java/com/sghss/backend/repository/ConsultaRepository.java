package com.sghss.backend.repository;

import com.sghss.backend.domain.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByProfissionalId(Long profissionalId);
    List<Consulta> findByPacienteId(Long pacienteId);
} 