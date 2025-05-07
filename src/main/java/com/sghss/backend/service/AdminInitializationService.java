package com.sghss.backend.service;

import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.domain.enums.Role;
import com.sghss.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminInitializationService implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByEmail("admin@sghss.com")) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setEmail("admin@sghss.com");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMINISTRADOR);
            usuarioRepository.save(admin);
        }
    }
} 