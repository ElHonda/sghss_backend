package com.sghss.backend.service;

import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.domain.enums.Role;
import com.sghss.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthService authService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Test User");
        usuario.setEmail("test@example.com");
        usuario.setSenha("password");
        usuario.setRole(Role.ADMINISTRADOR);
    }

    @Test
    void shouldLoadUserByUsername() {
        // given
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        // when
        UserDetails userDetails = authService.loadUserByUsername(usuario.getEmail());

        // then
        assertNotNull(userDetails);
        assertEquals(usuario.getEmail(), userDetails.getUsername());
        assertEquals(usuario.getSenha(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + Role.ADMINISTRADOR.name())));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(UsernameNotFoundException.class, () ->
                authService.loadUserByUsername("nonexistent@example.com"));
    }
} 