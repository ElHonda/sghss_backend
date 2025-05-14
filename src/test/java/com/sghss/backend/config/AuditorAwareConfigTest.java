package com.sghss.backend.config;

import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.domain.enums.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditorAwareConfigTest {

    private AuditorAwareConfig auditorAwareConfig;
    private AuditorAware<Usuario> auditorAware;
    private MockedStatic<SecurityContextHolder> securityContextHolderMockedStatic;
    private SecurityContext securityContext;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        auditorAwareConfig = new AuditorAwareConfig();
        auditorAware = auditorAwareConfig.auditorProvider();
        securityContextHolderMockedStatic = Mockito.mockStatic(SecurityContextHolder.class);
        securityContext = mock(SecurityContext.class);
        authentication = mock(Authentication.class);
        securityContextHolderMockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    }

    @AfterEach
    void tearDown() {
        securityContextHolderMockedStatic.close();
    }

    @Test
    void deveRetornarEmptyQuandoNaoHaAutenticacao() {
        when(securityContext.getAuthentication()).thenReturn(null);
        Optional<Usuario> auditor = auditorAware.getCurrentAuditor();
        assertTrue(auditor.isEmpty());
    }

    @Test
    void deveRetornarEmptyQuandoNaoEstaAutenticado() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        Optional<Usuario> auditor = auditorAware.getCurrentAuditor();
        assertTrue(auditor.isEmpty());
    }

    @Test
    void deveRetornarEmptyQuandoPrincipalNaoEhUsuario() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("string_qualquer");
        Optional<Usuario> auditor = auditorAware.getCurrentAuditor();
        assertTrue(auditor.isEmpty());
    }

    @Test
    void deveRetornarUsuarioQuandoPrincipalEhUsuario() {
        Usuario usuario = new Usuario(1L, "Test User", "test@example.com", "password", Role.ADMINISTRADOR);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(usuario);
        Optional<Usuario> auditor = auditorAware.getCurrentAuditor();
        assertTrue(auditor.isPresent());
        assertEquals(usuario, auditor.get());
    }
} 