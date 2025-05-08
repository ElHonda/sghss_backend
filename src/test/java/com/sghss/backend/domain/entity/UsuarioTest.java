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
    void shouldCreateUsuarioWithDefaultConstructor() {
        Usuario usuario = new Usuario();
        
        assertNotNull(usuario);
        assertNull(usuario.getId());
        assertNull(usuario.getNome());
        assertNull(usuario.getEmail());
        assertNull(usuario.getSenha());
        assertNull(usuario.getRole());
    }

    @Test
    void shouldGetAuthoritiesWithRole() {
        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();
        
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR")));
    }

    @Test
    void shouldGetAuthoritiesWithNullRole() {
        usuario.setRole(null);
        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();
        
        assertNotNull(authorities);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void shouldImplementUserDetailsInterface() {
        assertTrue(usuario instanceof UserDetails);
        assertEquals("test@example.com", usuario.getUsername());
        assertEquals("password", usuario.getPassword());
        assertTrue(usuario.isAccountNonExpired());
        assertTrue(usuario.isAccountNonLocked());
        assertTrue(usuario.isCredentialsNonExpired());
        assertTrue(usuario.isEnabled());
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
    void shouldHandleEqualsWithNull() {
        assertNotEquals(usuario, null);
    }

    @Test
    void shouldHandleEqualsWithDifferentClass() {
        assertNotEquals(usuario, new Object());
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
        other.setSenha("different_password");
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
        other.setRole(Role.PROFISSIONAL);
        
        assertNotEquals(usuario, other);
    }

    @Test
    void shouldHandleEqualsWithNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        
        assertEquals(usuario1, usuario2);
        
        usuario1.setId(1L);
        assertNotEquals(usuario1, usuario2);
        
        usuario2.setId(1L);
        assertEquals(usuario1, usuario2);
        
        usuario1.setNome("Test");
        assertNotEquals(usuario1, usuario2);
        
        usuario2.setNome("Test");
        assertEquals(usuario1, usuario2);
        
        usuario1.setEmail("test@example.com");
        assertNotEquals(usuario1, usuario2);
        
        usuario2.setEmail("test@example.com");
        assertEquals(usuario1, usuario2);
        
        usuario1.setSenha("password");
        assertNotEquals(usuario1, usuario2);
        
        usuario2.setSenha("password");
        assertEquals(usuario1, usuario2);
        
        usuario1.setRole(Role.ADMINISTRADOR);
        assertNotEquals(usuario1, usuario2);
        
        usuario2.setRole(Role.ADMINISTRADOR);
        assertEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
        
        usuario1.setId(1L);
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
        
        usuario2.setId(1L);
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
        
        usuario1.setNome("Test");
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
        
        usuario2.setNome("Test");
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
        
        usuario1.setEmail("test@example.com");
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
        
        usuario2.setEmail("test@example.com");
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
        
        usuario1.setSenha("password");
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
        
        usuario2.setSenha("password");
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
        
        usuario1.setRole(Role.ADMINISTRADOR);
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
        
        usuario2.setRole(Role.ADMINISTRADOR);
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleHashCodeWithAllNullFields() {
        Usuario usuario = new Usuario();
        assertNotEquals(0, usuario.hashCode());
    }

    @Test
    void shouldHandleHashCodeWithSomeNullFields() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome(null);
        usuario.setEmail(null);
        usuario.setSenha(null);
        usuario.setRole(null);
        
        assertNotEquals(0, usuario.hashCode());
    }

    @Test
    void shouldHandleToString() {
        String toString = usuario.toString();
        
        assertTrue(toString.contains("id=" + usuario.getId()));
        assertTrue(toString.contains("nome=" + usuario.getNome()));
        assertTrue(toString.contains("email=" + usuario.getEmail()));
        assertTrue(toString.contains("senha=" + usuario.getSenha()));
        assertTrue(toString.contains("role=" + usuario.getRole()));
    }

    @Test
    void shouldHandleToStringWithNullFields() {
        Usuario usuario = new Usuario();
        String toString = usuario.toString();
        
        assertTrue(toString.contains("id=null"));
        assertTrue(toString.contains("nome=null"));
        assertTrue(toString.contains("email=null"));
        assertTrue(toString.contains("senha=null"));
        assertTrue(toString.contains("role=null"));
    }

    @Test
    void shouldHandleEqualsWithNullId() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setNome("Test");
        usuario2.setNome("Test");
        
        assertEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleEqualsWithNullNome() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario2.setId(1L);
        
        assertEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleEqualsWithNullEmail() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario2.setId(1L);
        usuario2.setNome("Test");
        
        assertEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleEqualsWithNullSenha() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail("test@example.com");
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail("test@example.com");
        
        assertEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleEqualsWithNullRole() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail("test@example.com");
        usuario1.setSenha("password");
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail("test@example.com");
        usuario2.setSenha("password");
        
        assertEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithNullId() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setNome("Test");
        usuario2.setNome("Test");
        
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleHashCodeWithNullNome() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario2.setId(1L);
        
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleHashCodeWithNullEmail() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario2.setId(1L);
        usuario2.setNome("Test");
        
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleHashCodeWithNullSenha() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail("test@example.com");
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail("test@example.com");
        
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleHashCodeWithNullRole() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail("test@example.com");
        usuario1.setSenha("password");
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail("test@example.com");
        usuario2.setSenha("password");
        
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleToStringWithNullId() {
        Usuario usuario = new Usuario();
        usuario.setNome("Test");
        usuario.setEmail("test@example.com");
        usuario.setSenha("password");
        usuario.setRole(Role.ADMINISTRADOR);
        
        String toString = usuario.toString();
        assertTrue(toString.contains("id=null"));
        assertTrue(toString.contains("nome=Test"));
        assertTrue(toString.contains("email=test@example.com"));
        assertTrue(toString.contains("senha=password"));
        assertTrue(toString.contains("role=ADMINISTRADOR"));
    }

    @Test
    void shouldHandleToStringWithNullNome() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@example.com");
        usuario.setSenha("password");
        usuario.setRole(Role.ADMINISTRADOR);
        
        String toString = usuario.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nome=null"));
        assertTrue(toString.contains("email=test@example.com"));
        assertTrue(toString.contains("senha=password"));
        assertTrue(toString.contains("role=ADMINISTRADOR"));
    }

    @Test
    void shouldHandleToStringWithNullEmail() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Test");
        usuario.setSenha("password");
        usuario.setRole(Role.ADMINISTRADOR);
        
        String toString = usuario.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nome=Test"));
        assertTrue(toString.contains("email=null"));
        assertTrue(toString.contains("senha=password"));
        assertTrue(toString.contains("role=ADMINISTRADOR"));
    }

    @Test
    void shouldHandleToStringWithNullSenha() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Test");
        usuario.setEmail("test@example.com");
        usuario.setRole(Role.ADMINISTRADOR);
        
        String toString = usuario.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nome=Test"));
        assertTrue(toString.contains("email=test@example.com"));
        assertTrue(toString.contains("senha=null"));
        assertTrue(toString.contains("role=ADMINISTRADOR"));
    }

    @Test
    void shouldHandleToStringWithNullRole() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Test");
        usuario.setEmail("test@example.com");
        usuario.setSenha("password");
        
        String toString = usuario.toString();
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("nome=Test"));
        assertTrue(toString.contains("email=test@example.com"));
        assertTrue(toString.contains("senha=password"));
        assertTrue(toString.contains("role=null"));
    }

    @Test
    void shouldHandleEqualsWithDifferentIdAndNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario2.setId(2L);
        
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleEqualsWithDifferentNomeAndNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test1");
        usuario2.setId(1L);
        usuario2.setNome("Test2");
        
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleEqualsWithDifferentEmailAndNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail("test1@example.com");
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail("test2@example.com");
        
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleEqualsWithDifferentSenhaAndNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail("test@example.com");
        usuario1.setSenha("senha1");
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail("test@example.com");
        usuario2.setSenha("senha2");
        
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleEqualsWithDifferentRoleAndNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail("test@example.com");
        usuario1.setSenha("senha");
        usuario1.setRole(Role.ADMINISTRADOR);
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail("test@example.com");
        usuario2.setSenha("senha");
        usuario2.setRole(Role.PROFISSIONAL);
        
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithDifferentIdAndNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario2.setId(2L);
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleHashCodeWithDifferentNomeAndNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test1");
        usuario2.setId(1L);
        usuario2.setNome("Test2");
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleHashCodeWithDifferentEmailAndNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail("test1@example.com");
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail("test2@example.com");
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleHashCodeWithDifferentSenhaAndNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail("test@example.com");
        usuario1.setSenha("senha1");
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail("test@example.com");
        usuario2.setSenha("senha2");
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleHashCodeWithDifferentRoleAndNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail("test@example.com");
        usuario1.setSenha("senha");
        usuario1.setRole(Role.ADMINISTRADOR);
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail("test@example.com");
        usuario2.setSenha("senha");
        usuario2.setRole(Role.PROFISSIONAL);
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithAllFieldsDifferent() {
        Usuario usuario1 = new Usuario(1L, "Test1", "test1@example.com", "senha1", Role.ADMINISTRADOR);
        Usuario usuario2 = new Usuario(2L, "Test2", "test2@example.com", "senha2", Role.PROFISSIONAL);
        
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithAllFieldsDifferent() {
        Usuario usuario1 = new Usuario(1L, "Test1", "test1@example.com", "senha1", Role.ADMINISTRADOR);
        Usuario usuario2 = new Usuario(2L, "Test2", "test2@example.com", "senha2", Role.PROFISSIONAL);
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithPartialNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario2.setId(1L);
        usuario2.setNome(null);
        
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithPartialNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario2.setId(1L);
        usuario2.setNome(null);
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithNullAndNonNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail("test@example.com");
        usuario1.setSenha("senha");
        usuario1.setRole(Role.ADMINISTRADOR);
        
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithNullAndNonNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail("test@example.com");
        usuario1.setSenha("senha");
        usuario1.setRole(Role.ADMINISTRADOR);
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithDifferentFieldsAndNulls() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome(null);
        usuario1.setEmail("test1@example.com");
        usuario1.setSenha(null);
        usuario1.setRole(Role.ADMINISTRADOR);
        
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail(null);
        usuario2.setSenha("senha");
        usuario2.setRole(null);
        
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithDifferentFieldsAndNulls() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome(null);
        usuario1.setEmail("test1@example.com");
        usuario1.setSenha(null);
        usuario1.setRole(Role.ADMINISTRADOR);
        
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail(null);
        usuario2.setSenha("senha");
        usuario2.setRole(null);
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithMixedNullAndNonNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail(null);
        usuario1.setSenha("senha");
        usuario1.setRole(null);
        
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail("test@example.com");
        usuario2.setSenha(null);
        usuario2.setRole(Role.ADMINISTRADOR);
        
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithMixedNullAndNonNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail(null);
        usuario1.setSenha("senha");
        usuario1.setRole(null);
        
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail("test@example.com");
        usuario2.setSenha(null);
        usuario2.setRole(Role.ADMINISTRADOR);
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithAlternatingNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome(null);
        usuario1.setEmail("test@example.com");
        usuario1.setSenha(null);
        usuario1.setRole(Role.ADMINISTRADOR);
        
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail(null);
        usuario2.setSenha("senha");
        usuario2.setRole(null);
        
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithAlternatingNullFields() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome(null);
        usuario1.setEmail("test@example.com");
        usuario1.setSenha(null);
        usuario1.setRole(Role.ADMINISTRADOR);
        
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail(null);
        usuario2.setSenha("senha");
        usuario2.setRole(null);
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithNullAndNonNullFieldsInDifferentOrder() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail(null);
        usuario1.setSenha("senha");
        usuario1.setRole(Role.ADMINISTRADOR);
        
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail("test@example.com");
        usuario2.setSenha(null);
        usuario2.setRole(Role.ADMINISTRADOR);
        
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithNullAndNonNullFieldsInDifferentOrder() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome("Test");
        usuario1.setEmail(null);
        usuario1.setSenha("senha");
        usuario1.setRole(Role.ADMINISTRADOR);
        
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail("test@example.com");
        usuario2.setSenha(null);
        usuario2.setRole(Role.ADMINISTRADOR);
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithAllFieldsNullExceptOne() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario2.setId(2L);
        
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithAllFieldsNullExceptOne() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario2.setId(2L);
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithNullFieldsInDifferentPositions() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome(null);
        usuario1.setEmail("test@example.com");
        usuario1.setSenha(null);
        usuario1.setRole(Role.ADMINISTRADOR);
        
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail(null);
        usuario2.setSenha("senha");
        usuario2.setRole(null);
        
        assertNotEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithNullFieldsInDifferentPositions() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome(null);
        usuario1.setEmail("test@example.com");
        usuario1.setSenha(null);
        usuario1.setRole(Role.ADMINISTRADOR);
        
        usuario2.setId(1L);
        usuario2.setNome("Test");
        usuario2.setEmail(null);
        usuario2.setSenha("senha");
        usuario2.setRole(null);
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithNullFieldsInAllPositions() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(null);
        usuario1.setNome(null);
        usuario1.setEmail(null);
        usuario1.setSenha(null);
        usuario1.setRole(null);
        
        usuario2.setId(null);
        usuario2.setNome(null);
        usuario2.setEmail(null);
        usuario2.setSenha(null);
        usuario2.setRole(null);
        
        assertEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithNullFieldsInAllPositions() {
        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();
        usuario1.setId(null);
        usuario1.setNome(null);
        usuario1.setEmail(null);
        usuario1.setSenha(null);
        usuario1.setRole(null);
        
        usuario2.setId(null);
        usuario2.setNome(null);
        usuario2.setEmail(null);
        usuario2.setSenha(null);
        usuario2.setRole(null);
        
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleUserDetailsMethodsWithNullFields() {
        Usuario usuario = new Usuario();
        assertNull(usuario.getUsername());
        assertNull(usuario.getPassword());
        assertTrue(usuario.isAccountNonExpired());
        assertTrue(usuario.isAccountNonLocked());
        assertTrue(usuario.isCredentialsNonExpired());
        assertTrue(usuario.isEnabled());
    }

    @Test
    void shouldHandleEqualsWithNullAndSameId() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome(null);
        usuario1.setEmail(null);
        usuario1.setSenha(null);
        usuario1.setRole(null);

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);
        usuario2.setNome(null);
        usuario2.setEmail(null);
        usuario2.setSenha(null);
        usuario2.setRole(null);

        assertEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithNullAndSameId() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNome(null);
        usuario1.setEmail(null);
        usuario1.setSenha(null);
        usuario1.setRole(null);

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);
        usuario2.setNome(null);
        usuario2.setEmail(null);
        usuario2.setSenha(null);
        usuario2.setRole(null);

        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithAllFieldsNullExceptRole() {
        Usuario usuario1 = new Usuario();
        usuario1.setRole(Role.ADMINISTRADOR);

        Usuario usuario2 = new Usuario();
        usuario2.setRole(Role.ADMINISTRADOR);

        assertEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithAllFieldsNullExceptRole() {
        Usuario usuario1 = new Usuario();
        usuario1.setRole(Role.ADMINISTRADOR);

        Usuario usuario2 = new Usuario();
        usuario2.setRole(Role.ADMINISTRADOR);

        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithAllFieldsNullExceptEmail() {
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("test@example.com");

        Usuario usuario2 = new Usuario();
        usuario2.setEmail("test@example.com");

        assertEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithAllFieldsNullExceptEmail() {
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("test@example.com");

        Usuario usuario2 = new Usuario();
        usuario2.setEmail("test@example.com");

        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithAllFieldsNullExceptNome() {
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Test User");

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Test User");

        assertEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithAllFieldsNullExceptNome() {
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Test User");

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Test User");

        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }

    @Test
    void shouldHandleEqualsWithAllFieldsNullExceptSenha() {
        Usuario usuario1 = new Usuario();
        usuario1.setSenha("password");

        Usuario usuario2 = new Usuario();
        usuario2.setSenha("password");

        assertEquals(usuario1, usuario2);
    }

    @Test
    void shouldHandleHashCodeWithAllFieldsNullExceptSenha() {
        Usuario usuario1 = new Usuario();
        usuario1.setSenha("password");

        Usuario usuario2 = new Usuario();
        usuario2.setSenha("password");

        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }
} 