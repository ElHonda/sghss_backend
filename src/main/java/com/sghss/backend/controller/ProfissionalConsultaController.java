package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import com.sghss.backend.dto.ConsultaRequestDTO;
import com.sghss.backend.dto.ConsultaResponseDTO;
import com.sghss.backend.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissional/consultas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PROFISSIONAL')")
public class ProfissionalConsultaController {
    private final ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<ApiResponse<ConsultaResponseDTO>> agendarConsulta(@RequestBody ConsultaRequestDTO dto, @RequestParam Long profissionalId) {
        ConsultaResponseDTO response = consultaService.criar(profissionalId, dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Consulta agendada com sucesso"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ConsultaResponseDTO>>> listarConsultas(@RequestParam Long profissionalId) {
        List<ConsultaResponseDTO> consultas = consultaService.listarPorProfissional(profissionalId);
        return ResponseEntity.ok(ApiResponse.success(consultas));
    }
} 