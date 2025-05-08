package com.sghss.backend.service;

import com.sghss.backend.config.JwtConfig;
import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.domain.enums.Role;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "password";
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private JwtService jwtService;

    private Usuario usuario;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail(TEST_EMAIL);
        usuario.setSenha(TEST_PASSWORD);
        usuario.setRole(Role.ADMINISTRADOR);

        userDetails = new User(usuario.getEmail(), usuario.getSenha(), new ArrayList<>());
    }

    @Test
    void shouldGenerateToken() {
        // given
        when(jwtConfig.getSecret()).thenReturn(SECRET_KEY);
        when(jwtConfig.getExpiration()).thenReturn(86400000L); // 24 hours

        // when
        String token = jwtService.generateToken(usuario);

        // then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldValidateToken() {
        // given
        when(jwtConfig.getSecret()).thenReturn(SECRET_KEY);
        when(jwtConfig.getExpiration()).thenReturn(86400000L); // 24 hours
        String token = jwtService.generateToken(usuario);

        // when
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // then
        assertTrue(isValid);
    }

    @Test
    void shouldExtractUsername() {
        // given
        when(jwtConfig.getSecret()).thenReturn(SECRET_KEY);
        when(jwtConfig.getExpiration()).thenReturn(86400000L); // 24 hours
        String token = jwtService.generateToken(usuario);

        // when
        String username = jwtService.extractUsername(token);

        // then
        assertEquals(TEST_EMAIL, username);
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        // given
        String invalidToken = "invalid.token.here";

        // when & then
        assertThrows(Exception.class, () -> jwtService.extractUsername(invalidToken));
    }

    @Test
    void shouldThrowExceptionForExpiredToken() {
        // given
        when(jwtConfig.getSecret()).thenReturn(SECRET_KEY);
        
        // Criar um token com data de expiração fixa no passado
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        Date pastDate = new Date(0); // 1 de janeiro de 1970
        String expiredToken = Jwts.builder()
                .setSubject(TEST_EMAIL)
                .setIssuedAt(pastDate)
                .setExpiration(pastDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // when & then
        assertThrows(ExpiredJwtException.class, () -> jwtService.extractUsername(expiredToken));
    }
} 