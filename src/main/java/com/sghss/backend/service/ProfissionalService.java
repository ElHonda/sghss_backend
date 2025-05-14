package com.sghss.backend.service;

import com.sghss.backend.domain.entity.Profissional;
import com.sghss.backend.domain.entity.TipoProfissional;
import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.domain.enums.Role;
import com.sghss.backend.dto.ProfissionalRequestDTO;
import com.sghss.backend.dto.ProfissionalResponseDTO;
import com.sghss.backend.repository.ProfissionalRepository;
import com.sghss.backend.repository.TipoProfissionalRepository;
import com.sghss.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfissionalService {
    private final ProfissionalRepository profissionalRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoProfissionalRepository tipoProfissionalRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfissionalResponseDTO criar(ProfissionalRequestDTO dto) {
        // Criação automática do usuário
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setRole(Role.PROFISSIONAL);
        usuario = usuarioRepository.save(usuario);

        TipoProfissional tipo = tipoProfissionalRepository.findById(dto.getTipoProfissionalId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de profissional não encontrado"));
        Profissional profissional = new Profissional();
        profissional.setUsuario(usuario);
        profissional.setTipoProfissional(tipo);
        profissional.setRegistroProfissional(dto.getRegistroProfissional());
        profissional = profissionalRepository.save(profissional);
        return toResponseDTO(profissional);
    }

    public List<ProfissionalResponseDTO> listarTodos() {
        return profissionalRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProfissionalResponseDTO> buscarPorId(Long id) {
        return profissionalRepository.findById(id)
                .map(this::toResponseDTO);
    }

    public Optional<ProfissionalResponseDTO> atualizar(Long id, ProfissionalRequestDTO dto) {
        return profissionalRepository.findById(id)
                .map(profissional -> {
                    if (dto.getTipoProfissionalId() != null) {
                        TipoProfissional tipo = tipoProfissionalRepository.findById(dto.getTipoProfissionalId())
                                .orElseThrow(() -> new IllegalArgumentException("Tipo de profissional não encontrado"));
                        profissional.setTipoProfissional(tipo);
                    }
                    if (dto.getRegistroProfissional() != null) {
                        profissional.setRegistroProfissional(dto.getRegistroProfissional());
                    }
                    return profissionalRepository.save(profissional);
                })
                .map(this::toResponseDTO);
    }

    public boolean deletar(Long id) {
        return profissionalRepository.findById(id)
                .map(profissional -> {
                    profissionalRepository.delete(profissional);
                    return true;
                }).orElse(false);
    }

    private ProfissionalResponseDTO toResponseDTO(Profissional profissional) {
        ProfissionalResponseDTO dto = new ProfissionalResponseDTO();
        dto.setId(profissional.getId());
        dto.setUsuarioId(profissional.getUsuario().getId());
        dto.setNomeUsuario(profissional.getUsuario().getNome());
        dto.setEmailUsuario(profissional.getUsuario().getEmail());
        if (profissional.getTipoProfissional() != null) {
            dto.setTipoProfissionalId(profissional.getTipoProfissional().getId());
            dto.setTipoProfissionalNome(profissional.getTipoProfissional().getNome());
        }
        dto.setRegistroProfissional(profissional.getRegistroProfissional());
        return dto;
    }
} 