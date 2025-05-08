package com.sghss.backend.domain.entity;

import com.sghss.backend.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

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
    void shouldGetAuthorities() {
        // when
        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();

        // then
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + Role.ADMINISTRADOR.name())));
    }

    @Test
    void shouldGetPassword() {
        // when
        String password = usuario.getPassword();

        // then
        assertEquals(usuario.getSenha(), password);
    }

    @Test
    void shouldGetUsername() {
        // when
        String username = usuario.getUsername();

        // then
        assertEquals(usuario.getEmail(), username);
    }

    @Test
    void shouldReturnTrueForAccountNonExpired() {
        assertTrue(usuario.isAccountNonExpired());
    }

    @Test
    void shouldReturnTrueForAccountNonLocked() {
        assertTrue(usuario.isAccountNonLocked());
    }

    @Test
    void shouldReturnTrueForCredentialsNonExpired() {
        assertTrue(usuario.isCredentialsNonExpired());
    }

    @Test
    void shouldReturnTrueForEnabled() {
        assertTrue(usuario.isEnabled());
    }
} 