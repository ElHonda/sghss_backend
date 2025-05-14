package com.sghss.backend.repository;

import com.sghss.backend.domain.entity.TipoProfissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoProfissionalRepository extends JpaRepository<TipoProfissional, Long> {
} 