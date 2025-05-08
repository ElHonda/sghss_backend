package com.sghss.backend.domain.entity;

import com.sghss.backend.domain.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
    void shouldCreateUsuarioWithAllArgsConstructor() {
        Usuario usuario = new Usuario(1L, "Test User", "test@example.com", "password", Role.ADMINISTRADOR);
        
        assertNotNull(usuario);
        assertEquals(1L, usuario.getId());
        assertEquals("Test User", usuario.getNome());
        assertEquals("test@example.com", usuario.getEmail());
        assertEquals("password", usuario.getSenha());
        assertEquals(Role.ADMINISTRADOR, usuario.getRole());
    }

    @Test
    void shouldCreateUsuarioWithNoArgsConstructor() {
        Usuario usuario = new Usuario();
        
        assertNotNull(usuario);
        assertNull(usuario.getId());
        assertNull(usuario.getNome());
        assertNull(usuario.getEmail());
        assertNull(usuario.getSenha());
        assertNull(usuario.getRole());
    }

    @Test
    void shouldGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();
        
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> auth instanceof SimpleGrantedAuthority &&
                        auth.getAuthority().equals("ROLE_ADMINISTRADOR")));
    }

    @Test
    void shouldGetPassword() {
        assertEquals("password", usuario.getPassword());
    }

    @Test
    void shouldGetUsername() {
        assertEquals("test@example.com", usuario.getUsername());
    }

    @Test
    void shouldBeAccountNonExpired() {
        assertTrue(usuario.isAccountNonExpired());
    }

    @Test
    void shouldBeAccountNonLocked() {
        assertTrue(usuario.isAccountNonLocked());
    }

    @Test
    void shouldBeCredentialsNonExpired() {
        assertTrue(usuario.isCredentialsNonExpired());
    }

    @Test
    void shouldBeEnabled() {
        assertTrue(usuario.isEnabled());
    }

    @Test
    void shouldUpdateUsuarioFields() {
        usuario.setNome("Updated Name");
        usuario.setEmail("updated@example.com");
        usuario.setSenha("newpassword");
        usuario.setRole(Role.PROFISSIONAL);

        assertEquals("Updated Name", usuario.getNome());
        assertEquals("updated@example.com", usuario.getEmail());
        assertEquals("newpassword", usuario.getSenha());
        assertEquals(Role.PROFISSIONAL, usuario.getRole());
    }

    @Test
    void shouldHaveCorrectAuthoritiesForDifferentRoles() {
        usuario.setRole(Role.PROFISSIONAL);
        Collection<? extends GrantedAuthority> profAuthorities = usuario.getAuthorities();
        assertTrue(profAuthorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PROFISSIONAL")));

        usuario.setRole(Role.PACIENTE);
        Collection<? extends GrantedAuthority> pacAuthorities = usuario.getAuthorities();
        assertTrue(pacAuthorities.stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_PACIENTE")));
    }

    @Test
    void shouldHandleNullRole() {
        usuario.setRole(null);
        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void shouldHandleNullFields() {
        Usuario usuario = new Usuario();
        assertNull(usuario.getNome());
        assertNull(usuario.getEmail());
        assertNull(usuario.getSenha());
        assertNull(usuario.getRole());
        assertNull(usuario.getId());
    }

    @Test
    void shouldHandleEmptyFields() {
        usuario.setNome("");
        usuario.setEmail("");
        usuario.setSenha("");
        
        assertEquals("", usuario.getNome());
        assertEquals("", usuario.getEmail());
        assertEquals("", usuario.getSenha());
    }

    @Test
    void shouldHandleSpecialCharactersInFields() {
        usuario.setNome("João Silva");
        usuario.setEmail("joão.silva@exemplo.com");
        usuario.setSenha("senha@123");
        
        assertEquals("João Silva", usuario.getNome());
        assertEquals("joão.silva@exemplo.com", usuario.getEmail());
        assertEquals("senha@123", usuario.getSenha());
    }

    @Test
    void shouldHandleLongFields() {
        String longName = "a".repeat(255);
        String longEmail = "a".repeat(255);
        String longPassword = "a".repeat(255);
        
        usuario.setNome(longName);
        usuario.setEmail(longEmail);
        usuario.setSenha(longPassword);
        
        assertEquals(longName, usuario.getNome());
        assertEquals(longEmail, usuario.getEmail());
        assertEquals(longPassword, usuario.getSenha());
    }

    @Test
    void shouldHandleEqualsAndHashCode() {
        Usuario usuario1 = new Usuario(1L, "Test User", "test@example.com", "password", Role.ADMINISTRADOR);
        Usuario usuario2 = new Usuario(1L, "Test User", "test@example.com", "password", Role.ADMINISTRADOR);
        Usuario usuario3 = new Usuario(2L, "Other User", "other@example.com", "password", Role.PROFISSIONAL);

        assertEquals(usuario1, usuario2);
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
        assertNotEquals(usuario1, usuario3);
        assertNotEquals(usuario1.hashCode(), usuario3.hashCode());
    }

    @Test
    void shouldHandleToString() {
        Usuario usuario = new Usuario(1L, "Test User", "test@example.com", "password", Role.ADMINISTRADOR);
        String toString = usuario.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nome=Test User"));
        assertTrue(toString.contains("email=test@example.com"));
        assertTrue(toString.contains("senha=password"));
        assertTrue(toString.contains("role=ADMINISTRADOR"));
    }

    @Test
    void shouldHandleUserDetailsImplementation() {
        assertInstanceOf(UserDetails.class, usuario);
        UserDetails userDetails = usuario;

        assertEquals(usuario.getEmail(), userDetails.getUsername());
        assertEquals(usuario.getSenha(), userDetails.getPassword());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void shouldHandleRoleComparison() {
        Usuario admin = new Usuario(1L, "Admin", "admin@example.com", "password", Role.ADMINISTRADOR);
        Usuario prof = new Usuario(2L, "Prof", "prof@example.com", "password", Role.PROFISSIONAL);
        Usuario pac = new Usuario(3L, "Pac", "pac@example.com", "password", Role.PACIENTE);

        assertNotEquals(admin.getRole(), prof.getRole());
        assertNotEquals(prof.getRole(), pac.getRole());
        assertNotEquals(admin.getRole(), pac.getRole());
    }

    @Test
    void shouldHandleAuthoritiesFormat() {
        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();
        GrantedAuthority authority = authorities.iterator().next();

        assertEquals("ROLE_ADMINISTRADOR", authority.getAuthority());
        assertInstanceOf(SimpleGrantedAuthority.class, authority);
    }

    @Test
    void shouldHandleEqualsWithNull() {
        assertNotEquals(null, usuario);
    }

    @Test
    void shouldHandleEqualsWithDifferentClass() {
        assertNotEquals(new Object(), usuario);
    }

    @Test
    void shouldHandleEqualsWithSameObject() {
        assertEquals(usuario, usuario);
    }

    @Test
    void shouldHandleEqualsWithDifferentId() {
        Usuario other = new Usuario();
        other.setId(2L);
        other.setNome(usuario.getNome());
        other.setEmail(usuario.getEmail());
        other.setSenha(usuario.getSenha());
        other.setRole(usuario.getRole());

        assertNotEquals(usuario, other);
    }

    @Test
    void shouldHandleEqualsWithDifferentNome() {
        Usuario other = new Usuario();
        other.setId(usuario.getId());
        other.setNome("Different Name");
        other.setEmail(usuario.getEmail());
        other.setSenha(usuario.getSenha());
        other.setRole(usuario.getRole());

        assertNotEquals(usuario, other);
    }

    @Test
    void shouldHandleEqualsWithDifferentEmail() {
        Usuario other = new Usuario();
        other.setId(usuario.getId());
        other.setNome(usuario.getNome());
        other.setEmail("different@example.com");
        other.setSenha(usuario.getSenha());
        other.setRole(usuario.getRole());

        assertNotEquals(usuario, other);
    }

    @Test
    void shouldHandleEqualsWithDifferentSenha() {
        Usuario other = new Usuario();
        other.setId(usuario.getId());
        other.setNome(usuario.getNome());
        other.setEmail(usuario.getEmail());
        other.setSenha("differentpassword");
        other.setRole(usuario.getRole());

        assertNotEquals(usuario, other);
    }

    @Test
    void shouldHandleEqualsWithDifferentRole() {
        Usuario other = new Usuario();
        other.setId(usuario.getId());
        other.setNome(usuario.getNome());
        other.setEmail(usuario.getEmail());
        other.setSenha(usuario.getSenha());
        other.setRole(Role.PACIENTE);

        assertNotEquals(usuario, other);
    }

    @Test
    void shouldHandleHashCodeWithNullFields() {
        Usuario nullUser = new Usuario();
        assertNotEquals(0, nullUser.hashCode());
    }

    @Test
    void shouldHandleHashCodeConsistency() {
        int hash1 = usuario.hashCode();
        int hash2 = usuario.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void shouldHandleHashCodeWithAllFields() {
        Usuario user1 = new Usuario(1L, "Test", "test@example.com", "password", Role.ADMINISTRADOR);
        Usuario user2 = new Usuario(1L, "Test", "test@example.com", "password", Role.ADMINISTRADOR);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
} 