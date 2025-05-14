package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import com.sghss.backend.dto.ProfissionalRequestDTO;
import com.sghss.backend.dto.ProfissionalResponseDTO;
import com.sghss.backend.service.ProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/profissionais")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProfissionalResponseDTO>> criar(@RequestBody ProfissionalRequestDTO dto) {
        ProfissionalResponseDTO response = profissionalService.criar(dto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProfissionalResponseDTO>>> listarTodos() {
        List<ProfissionalResponseDTO> lista = profissionalService.listarTodos();
        return ResponseEntity.ok(ApiResponse.success(lista));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfissionalResponseDTO>> buscarPorId(@PathVariable Long id) {
        return profissionalService.buscarPorId(id)
                .map(dto -> ResponseEntity.ok(ApiResponse.success(dto)))
                .orElseGet(() -> ResponseEntity.status(404).body(ApiResponse.error(404, "Profissional não encontrado")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProfissionalResponseDTO>> atualizar(@PathVariable Long id, @RequestBody ProfissionalRequestDTO dto) {
        return profissionalService.atualizar(id, dto)
                .map(updated -> ResponseEntity.ok(ApiResponse.success(updated)))
                .orElseGet(() -> ResponseEntity.status(404).body(ApiResponse.error(404, "Profissional não encontrado")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        boolean deleted = profissionalService.deletar(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success(null, "Profissional deletado com sucesso"));
        } else {
            return ResponseEntity.status(404).body(ApiResponse.error(404, "Profissional não encontrado"));
        }
    }
} 