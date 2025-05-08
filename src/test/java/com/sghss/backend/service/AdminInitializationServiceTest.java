package com.sghss.backend.service;

import com.sghss.backend.domain.enums.Role;
import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminInitializationServiceTest {

    private static final String ADMIN_EMAIL = "admin@sghss.com";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ENCODED_PASSWORD = "encoded_password";

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminInitializationService adminInitializationService;

    @Captor
    private ArgumentCaptor<Usuario> usuarioCaptor;

    @Test
    void shouldCreateAdminUserWhenNoneExists() {
        // given
        when(usuarioRepository.existsByEmail(ADMIN_EMAIL)).thenReturn(false);
        when(passwordEncoder.encode(ADMIN_PASSWORD)).thenReturn(ENCODED_PASSWORD);

        // when
        adminInitializationService.run();

        // then
        verify(usuarioRepository).save(usuarioCaptor.capture());
        Usuario savedAdmin = usuarioCaptor.getValue();
        assertEquals("Administrador", savedAdmin.getNome());
        assertEquals(ADMIN_EMAIL, savedAdmin.getEmail());
        assertEquals(ENCODED_PASSWORD, savedAdmin.getSenha());
        assertEquals(Role.ADMINISTRADOR, savedAdmin.getRole());
    }

    @Test
    void shouldNotCreateAdminUserWhenAlreadyExists() {
        // given
        when(usuarioRepository.existsByEmail(ADMIN_EMAIL)).thenReturn(true);

        // when
        adminInitializationService.run();

        // then
        verify(usuarioRepository, never()).save(any());
    }
} 