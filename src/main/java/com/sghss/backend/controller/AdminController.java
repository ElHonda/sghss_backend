package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminController {

    @GetMapping("/teste")
    public ResponseEntity<ApiResponse<String>> testeAdmin() {
        return ResponseEntity.ok(ApiResponse.success("Rota de teste para administrador"));
    }
} 