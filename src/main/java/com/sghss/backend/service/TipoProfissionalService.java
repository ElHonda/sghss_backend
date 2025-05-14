package com.sghss.backend.service;

import com.sghss.backend.domain.entity.TipoProfissional;
import com.sghss.backend.dto.TipoProfissionalRequestDTO;
import com.sghss.backend.dto.TipoProfissionalResponseDTO;
import com.sghss.backend.repository.TipoProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoProfissionalService {
    private final TipoProfissionalRepository repository;

    public TipoProfissionalResponseDTO criar(TipoProfissionalRequestDTO dto) {
        TipoProfissional tipo = new TipoProfissional();
        tipo.setNome(dto.getNome());
        tipo = repository.save(tipo);
        return toResponseDTO(tipo);
    }

    public List<TipoProfissionalResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<TipoProfissionalResponseDTO> buscarPorId(Long id) {
        return repository.findById(id).map(this::toResponseDTO);
    }

    public Optional<TipoProfissionalResponseDTO> atualizar(Long id, TipoProfissionalRequestDTO dto) {
        return repository.findById(id)
                .map(tipo -> {
                    tipo.setNome(dto.getNome());
                    return repository.save(tipo);
                })
                .map(this::toResponseDTO);
    }

    public boolean deletar(Long id) {
        return repository.findById(id)
                .map(tipo -> {
                    repository.delete(tipo);
                    return true;
                }).orElse(false);
    }

    private TipoProfissionalResponseDTO toResponseDTO(TipoProfissional tipo) {
        TipoProfissionalResponseDTO dto = new TipoProfissionalResponseDTO();
        dto.setId(tipo.getId());
        dto.setNome(tipo.getNome());
        return dto;
    }
} 