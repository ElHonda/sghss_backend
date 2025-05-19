package com.sghss.backend.repository;

import com.sghss.backend.domain.entity.HistoricoClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoClinicoRepository extends JpaRepository<HistoricoClinico, Long> {
    List<HistoricoClinico> findByPacienteId(Long pacienteId);
} 