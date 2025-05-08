package com.sghss.backend.service;

import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.domain.enums.Role;
import com.sghss.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminInitializationServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminInitializationService adminInitializationService;

    @Test
    void shouldCreateAdminUserWhenNotExists() {
        // given
        when(usuarioRepository.existsByEmail("admin@sghss.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded_password");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        // when
        adminInitializationService.run();

        // then
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario savedUsuario = usuarioCaptor.getValue();
        assertNotNull(savedUsuario);
        assertEquals("Administrador", savedUsuario.getNome());
        assertEquals("admin@sghss.com", savedUsuario.getEmail());
        assertEquals("encoded_password", savedUsuario.getSenha());
        assertEquals(Role.ADMINISTRADOR, savedUsuario.getRole());
    }

    @Test
    void shouldNotCreateAdminUserWhenExists() {
        // given
        when(usuarioRepository.existsByEmail("admin@sghss.com")).thenReturn(true);

        // when
        adminInitializationService.run();

        // then
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void shouldHandleRepositoryException() {
        // given
        when(usuarioRepository.existsByEmail("admin@sghss.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded_password");
        when(usuarioRepository.save(any(Usuario.class))).thenThrow(new RuntimeException("Database error"));

        // when & then
        assertThrows(RuntimeException.class, () -> adminInitializationService.run());
    }

    @Test
    void shouldHandleNullPasswordEncoder() {
        // given
        when(usuarioRepository.existsByEmail("admin@sghss.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn(null);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> i.getArgument(0));

        // when
        adminInitializationService.run();

        // then
        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(usuarioCaptor.capture());

        Usuario savedUsuario = usuarioCaptor.getValue();
        assertNotNull(savedUsuario);
        assertNull(savedUsuario.getSenha());
    }
} 