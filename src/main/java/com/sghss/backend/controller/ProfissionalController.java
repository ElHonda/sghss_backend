package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profissional")
@PreAuthorize("hasRole('PROFISSIONAL')")
public class ProfissionalController {

    @GetMapping("/teste")
    public ResponseEntity<ApiResponse<String>> testeProfissional() {
        return ResponseEntity.ok(ApiResponse.success("Rota de teste para profissional"));
    }
} 