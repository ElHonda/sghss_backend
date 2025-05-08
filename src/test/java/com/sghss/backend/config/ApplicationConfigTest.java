package com.sghss.backend.config;

import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.domain.enums.Role;
import com.sghss.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationConfigTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private ApplicationConfig applicationConfig;

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
    void userDetailsServiceShouldLoadUserByUsername() {
        // given
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        // when
        UserDetails userDetails = applicationConfig.userDetailsService().loadUserByUsername(usuario.getEmail());

        // then
        assertNotNull(userDetails);
        assertEquals(usuario.getEmail(), userDetails.getUsername());
        assertEquals(usuario.getSenha(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + Role.ADMINISTRADOR.name())));
    }

    @Test
    void userDetailsServiceShouldThrowExceptionWhenUserNotFound() {
        // given
        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.empty());

        // when & then
        assertThrows(UsernameNotFoundException.class, () ->
                applicationConfig.userDetailsService().loadUserByUsername("nonexistent@example.com"));
    }

    @Test
    void authenticationProviderShouldBeConfigured() {
        // when
        AuthenticationProvider authProvider = applicationConfig.authenticationProvider();

        // then
        assertNotNull(authProvider);
    }

    @Test
    void authenticationManagerShouldBeConfigured() throws Exception {
        // given
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockAuthManager);

        // when
        AuthenticationManager authManager = applicationConfig.authenticationManager(authenticationConfiguration);

        // then
        assertNotNull(authManager);
        assertEquals(mockAuthManager, authManager);
    }

    @Test
    void passwordEncoderShouldBeConfigured() {
        // when
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();

        // then
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder.matches("password", passwordEncoder.encode("password")));
    }
} 