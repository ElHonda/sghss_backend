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
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void shouldCreateUserDetailsService() {
        // given
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        // when
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();
        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        // then
        assertNotNull(userDetailsService);
        assertNotNull(userDetails);
        assertEquals(usuario.getEmail(), userDetails.getUsername());
        assertEquals(usuario.getSenha(), userDetails.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // when
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();

        // then
        assertThrows(UsernameNotFoundException.class, () -> 
            userDetailsService.loadUserByUsername("nonexistent@example.com")
        );
    }

    @Test
    void shouldCreateAuthenticationProvider() {
        // when
        AuthenticationProvider authProvider = applicationConfig.authenticationProvider();

        // then
        assertNotNull(authProvider);
        assertInstanceOf(DaoAuthenticationProvider.class, authProvider);
    }

    @Test
    void shouldCreateAuthenticationManager() throws Exception {
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
    void shouldCreatePasswordEncoder() {
        // when
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();

        // then
        assertNotNull(passwordEncoder);
        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder);
    }

    @Test
    void shouldHandleNullEmailInUserDetailsService() {
        // when
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();

        // then
        assertThrows(IllegalArgumentException.class, () -> 
            userDetailsService.loadUserByUsername(null)
        );
    }

    @Test
    void shouldHandleEmptyEmailInUserDetailsService() {
        // when
        UserDetailsService userDetailsService = applicationConfig.userDetailsService();

        // then
        assertThrows(IllegalArgumentException.class, () -> 
            userDetailsService.loadUserByUsername("")
        );
    }
} 