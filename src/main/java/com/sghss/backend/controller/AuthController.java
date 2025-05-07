package com.sghss.backend.controller;

import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Email já cadastrado");
            return ResponseEntity.badRequest().body(response);
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário registrado com sucesso");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        var usuario = usuarioRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(loginRequest.senha(), usuario.getSenha())) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Senha incorreta");
            return ResponseEntity.badRequest().body(response);
        }

        // TODO: Implementar geração do token JWT
        String token = "jwt_token_aqui"; // Temporário até implementar a geração do token

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Login realizado com sucesso");
        response.put("token", token);
        response.put("user", Map.of(
            "id", usuario.getId(),
            "nome", usuario.getNome(),
            "email", usuario.getEmail(),
            "role", usuario.getRole()
        ));
        
        return ResponseEntity.ok(response);
    }

    record LoginRequest(String email, String senha) {}
} 