package com.sghss.backend.controller;

import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.dto.ApiResponse;
import com.sghss.backend.repository.UsuarioRepository;
import com.sghss.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody(required = false) Usuario usuario) {
        if (!isValidUser(usuario)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "Dados de usuário inválidos"));
        }

        if (usuario.getRole() == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "Role inválido"));
        }

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "Email já cadastrado"));
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        Usuario savedUser = usuarioRepository.save(usuario);

        return ResponseEntity.ok(ApiResponse.success(savedUser, "Usuário cadastrado com sucesso"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody(required = false) LoginRequest request) {
        if (!isValidLoginRequest(request)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "Credenciais inválidas"));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            Usuario usuario = usuarioRepository.findByEmail(request.email())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

            String token = jwtService.generateToken(usuario);
            Map<String, Object> data = Map.of("token", token);

            return ResponseEntity.ok(ApiResponse.success(data, "Login realizado com sucesso"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error(401, "Credenciais inválidas"));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error(404, "Usuário não encontrado"));
        }
    }

    private boolean isValidUser(Usuario usuario) {
        return usuario != null &&
               StringUtils.hasText(usuario.getNome()) &&
               StringUtils.hasText(usuario.getEmail()) &&
               StringUtils.hasText(usuario.getSenha());
    }

    private boolean isValidLoginRequest(LoginRequest request) {
        return request != null &&
               StringUtils.hasText(request.email()) &&
               StringUtils.hasText(request.password());
    }

    public record LoginRequest(String email, String password) {}
} 