package com.sghss.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paciente")
@PreAuthorize("hasRole('PACIENTE')")
public class PacienteController {

    @GetMapping("/teste")
    public ResponseEntity<String> testePaciente() {
        return ResponseEntity.ok("Rota de teste para paciente");
    }
} 