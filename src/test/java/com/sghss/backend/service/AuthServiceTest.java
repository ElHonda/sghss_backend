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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        // when
        UserDetails userDetails = authService.loadUserByUsername("test@example.com");

        // then
        assertNotNull(userDetails);
        assertEquals(usuario.getEmail(), userDetails.getUsername());
        assertEquals(usuario.getSenha(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMINISTRADOR")));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when & then
        assertThrows(UsernameNotFoundException.class, () -> 
            authService.loadUserByUsername("nonexistent@example.com")
        );
    }

    @Test
    void shouldHandleNullEmail() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> 
            authService.loadUserByUsername(null)
        );
    }

    @Test
    void shouldHandleEmptyEmail() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> 
            authService.loadUserByUsername("")
        );
    }
} 