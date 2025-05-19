package com.sghss.backend.service;

import com.sghss.backend.domain.entity.HistoricoClinico;
import com.sghss.backend.repository.HistoricoClinicoRepository;
import com.sghss.backend.dto.HistoricoClinicoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoricoClinicoService {
    private final HistoricoClinicoRepository historicoClinicoRepository;

    public List<HistoricoClinico> listarPorPaciente(Long pacienteId) {
        return historicoClinicoRepository.findByPacienteId(pacienteId);
    }

    public Optional<HistoricoClinico> buscarPorId(Long id) {
        return historicoClinicoRepository.findById(id);
    }

    public List<HistoricoClinicoDTO> listarDTOsPorPaciente(Long pacienteId) {
        return listarPorPaciente(pacienteId).stream().map(this::toDTO).toList();
    }

    public Optional<HistoricoClinicoDTO> buscarDTOporId(Long id) {
        return buscarPorId(id).map(this::toDTO);
    }

    private HistoricoClinicoDTO toDTO(HistoricoClinico h) {
        return new HistoricoClinicoDTO(
            h.getId(),
            h.getDescricao(),
            h.getData(),
            h.getPaciente() != null ? h.getPaciente().getId() : null,
            h.getPaciente() != null ? h.getPaciente().getNome() : null
        );
    }
} 