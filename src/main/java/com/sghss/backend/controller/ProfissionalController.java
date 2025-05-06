package com.sghss.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profissional")
@PreAuthorize("hasRole('PROFISSIONAL')")
public class ProfissionalController {

    @GetMapping("/teste")
    public ResponseEntity<String> testeProfissional() {
        return ResponseEntity.ok("Rota de teste para profissional");
    }
} 