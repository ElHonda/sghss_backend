package com.sghss.backend.service;

import com.sghss.backend.domain.entity.Consulta;
import com.sghss.backend.domain.entity.Paciente;
import com.sghss.backend.domain.entity.Profissional;
import com.sghss.backend.dto.ConsultaRequestDTO;
import com.sghss.backend.dto.ConsultaResponseDTO;
import com.sghss.backend.repository.ConsultaRepository;
import com.sghss.backend.repository.PacienteRepository;
import com.sghss.backend.repository.ProfissionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultaService {
    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalRepository profissionalRepository;

    @Transactional
    public ConsultaResponseDTO criar(Long profissionalId, ConsultaRequestDTO dto) {
        Profissional profissional = profissionalRepository.findById(profissionalId)
                .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
        Consulta consulta = new Consulta();
        consulta.setProfissional(profissional);
        consulta.setPaciente(paciente);
        consulta.setDataHora(dto.getDataHora());
        consulta.setStatus(dto.getStatus());
        consulta.setTeleconsulta(dto.isTeleconsulta());
        consulta.setVideochamadaUrl(dto.getVideochamadaUrl());
        consulta = consultaRepository.save(consulta);
        return toResponseDTO(consulta);
    }

    public List<ConsultaResponseDTO> listarPorProfissional(Long profissionalId) {
        return consultaRepository.findByProfissionalId(profissionalId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public List<ConsultaResponseDTO> listarPorPaciente(Long pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId)
                .stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    public Optional<ConsultaResponseDTO> buscarPorId(Long id) {
        return consultaRepository.findById(id).map(this::toResponseDTO);
    }

    private ConsultaResponseDTO toResponseDTO(Consulta c) {
        ConsultaResponseDTO dto = new ConsultaResponseDTO();
        dto.setId(c.getId());
        dto.setPacienteId(c.getPaciente() != null ? c.getPaciente().getId() : null);
        dto.setPacienteNome(c.getPaciente() != null ? c.getPaciente().getNome() : null);
        dto.setProfissionalId(c.getProfissional() != null ? c.getProfissional().getId() : null);
        dto.setProfissionalNome(
            c.getProfissional() != null && c.getProfissional().getUsuario() != null
                ? c.getProfissional().getUsuario().getNome()
                : null
        );
        dto.setDataHora(c.getDataHora());
        dto.setStatus(c.getStatus());
        dto.setTeleconsulta(c.isTeleconsulta());
        dto.setVideochamadaUrl(c.getVideochamadaUrl());
        return dto;
    }
} 