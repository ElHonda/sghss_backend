package com.sghss.backend.repository;

import com.sghss.backend.domain.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    boolean existsByUsuarioEmail(String email);
    Paciente findByUsuarioEmail(String email);
} 