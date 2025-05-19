package com.sghss.backend.service;

import com.sghss.backend.domain.entity.Paciente;
import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.domain.enums.Role;
import com.sghss.backend.dto.PacienteRequestDTO;
import com.sghss.backend.dto.PacienteResponseDTO;
import com.sghss.backend.repository.PacienteRepository;
import com.sghss.backend.repository.UsuarioRepository;
import com.sghss.backend.util.CryptoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public PacienteResponseDTO criar(PacienteRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setRole(Role.PACIENTE);
        usuario = usuarioRepository.save(usuario);

        Paciente paciente = new Paciente();
        paciente.setUsuario(usuario);
        paciente.setNome(dto.getNome());
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setCpf(CryptoUtil.encrypt(dto.getCpf()));
        paciente.setTelefone(CryptoUtil.encrypt(dto.getTelefone()));
        paciente.setEndereco(CryptoUtil.encrypt(dto.getEndereco()));
        paciente = pacienteRepository.save(paciente);
        return toResponseDTO(paciente);
    }

    public List<PacienteResponseDTO> listarTodos() {
        return pacienteRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<PacienteResponseDTO> buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .map(this::toResponseDTO);
    }

    @Transactional
    public PacienteResponseDTO atualizar(Long id, PacienteRequestDTO dto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Paciente não encontrado"));
        paciente.setNome(dto.getNome());
        paciente.setDataNascimento(dto.getDataNascimento());
        paciente.setCpf(CryptoUtil.encrypt(dto.getCpf()));
        paciente.setTelefone(CryptoUtil.encrypt(dto.getTelefone()));
        paciente.setEndereco(CryptoUtil.encrypt(dto.getEndereco()));
        paciente = pacienteRepository.save(paciente);
        return toResponseDTO(paciente);
    }

    @Transactional
    public void deletar(Long id) {
        pacienteRepository.deleteById(id);
    }

    private PacienteResponseDTO toResponseDTO(Paciente paciente) {
        PacienteResponseDTO dto = new PacienteResponseDTO();
        dto.setId(paciente.getUsuario().getId());
        dto.setNome(paciente.getNome());
        dto.setDataNascimento(paciente.getDataNascimento());
        dto.setCpf(CryptoUtil.decrypt(paciente.getCpf()));
        dto.setTelefone(CryptoUtil.decrypt(paciente.getTelefone()));
        dto.setEndereco(CryptoUtil.decrypt(paciente.getEndereco()));
        dto.setEmail(paciente.getUsuario().getEmail());
        return dto;
    }
} 