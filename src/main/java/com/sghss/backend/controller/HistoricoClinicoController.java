package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import com.sghss.backend.dto.HistoricoClinicoDTO;
import com.sghss.backend.service.HistoricoClinicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HistoricoClinicoController {
    private final HistoricoClinicoService historicoClinicoService;

    @GetMapping("/pacientes/{pacienteId}/historico-clinico")
    public ResponseEntity<ApiResponse<List<HistoricoClinicoDTO>>> listarPorPaciente(@PathVariable Long pacienteId) {
        List<HistoricoClinicoDTO> historicos = historicoClinicoService.listarDTOsPorPaciente(pacienteId);
        return ResponseEntity.ok(ApiResponse.success(historicos));
    }

    @GetMapping("/historico-clinico/{id}")
    public ResponseEntity<ApiResponse<HistoricoClinicoDTO>> buscarPorId(@PathVariable Long id) {
        return historicoClinicoService.buscarDTOporId(id)
                .map(dto -> ResponseEntity.ok(ApiResponse.success(dto)))
                .orElse(ResponseEntity.notFound().build());
    }
} 