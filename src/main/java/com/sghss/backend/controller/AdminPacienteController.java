package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import com.sghss.backend.dto.PacienteRequestDTO;
import com.sghss.backend.dto.PacienteResponseDTO;
import com.sghss.backend.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/pacientes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROFISSIONAL')")
public class AdminPacienteController {
    private final PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<ApiResponse<PacienteResponseDTO>> criar(@RequestBody PacienteRequestDTO dto) {
        PacienteResponseDTO response = pacienteService.criar(dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Paciente criado com sucesso"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PacienteResponseDTO>>> listarTodos() {
        List<PacienteResponseDTO> lista = pacienteService.listarTodos();
        return ResponseEntity.ok(ApiResponse.success(lista));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PacienteResponseDTO>> buscarPorId(@PathVariable Long id) {
        return pacienteService.buscarPorId(id)
                .map(dto -> ResponseEntity.ok(ApiResponse.success(dto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PacienteResponseDTO>> atualizar(@PathVariable Long id, @RequestBody PacienteRequestDTO dto) {
        PacienteResponseDTO response = pacienteService.atualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success(response, "Paciente atualizado com sucesso"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Paciente deletado com sucesso"));
    }
} 