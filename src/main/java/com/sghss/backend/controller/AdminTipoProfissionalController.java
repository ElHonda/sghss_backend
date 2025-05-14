package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import com.sghss.backend.dto.TipoProfissionalRequestDTO;
import com.sghss.backend.dto.TipoProfissionalResponseDTO;
import com.sghss.backend.service.TipoProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tipos-profissional")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AdminTipoProfissionalController {

    private final TipoProfissionalService tipoProfissionalService;

    @PostMapping
    public ResponseEntity<ApiResponse<TipoProfissionalResponseDTO>> criar(@RequestBody TipoProfissionalRequestDTO dto) {
        TipoProfissionalResponseDTO response = tipoProfissionalService.criar(dto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TipoProfissionalResponseDTO>>> listarTodos() {
        List<TipoProfissionalResponseDTO> lista = tipoProfissionalService.listarTodos();
        return ResponseEntity.ok(ApiResponse.success(lista));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoProfissionalResponseDTO>> buscarPorId(@PathVariable Long id) {
        return tipoProfissionalService.buscarPorId(id)
                .map(dto -> ResponseEntity.ok(ApiResponse.success(dto)))
                .orElseGet(() -> ResponseEntity.status(404).body(ApiResponse.error(404, "Tipo de profissional não encontrado")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoProfissionalResponseDTO>> atualizar(@PathVariable Long id, @RequestBody TipoProfissionalRequestDTO dto) {
        return tipoProfissionalService.atualizar(id, dto)
                .map(updated -> ResponseEntity.ok(ApiResponse.success(updated)))
                .orElseGet(() -> ResponseEntity.status(404).body(ApiResponse.error(404, "Tipo de profissional não encontrado")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletar(@PathVariable Long id) {
        boolean deleted = tipoProfissionalService.deletar(id);
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.success(null, "Tipo de profissional deletado com sucesso"));
        } else {
            return ResponseEntity.status(404).body(ApiResponse.error(404, "Tipo de profissional não encontrado"));
        }
    }
} 