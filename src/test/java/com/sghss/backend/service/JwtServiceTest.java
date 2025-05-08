package com.sghss.backend.service;

import com.sghss.backend.config.JwtConfig;
import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.domain.enums.Role;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
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
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    private Key key;
    private String validToken;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Test User");
        usuario.setEmail(TEST_EMAIL);
        usuario.setSenha(TEST_PASSWORD);
        usuario.setRole(Role.ADMINISTRADOR);

        userDetails = new User(usuario.getEmail(), usuario.getSenha(), new ArrayList<>());
        key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        when(jwtConfig.getSecret()).thenReturn(SECRET_KEY);
        when(jwtConfig.getExpiration()).thenReturn(3600000L);

        validToken = jwtService.generateToken(usuario);
    }

    @Test
    void shouldGenerateToken() {
        // when
        String token = jwtService.generateToken(usuario);

        // then
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void shouldValidateToken() {
        // when
        boolean isValid = jwtService.isTokenValid(validToken, userDetails);

        // then
        assertTrue(isValid);
    }

    @Test
    void shouldExtractUsername() {
        // when
        String username = jwtService.extractUsername(validToken);

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
        String expiredToken = Jwts.builder()
                .setSubject(TEST_EMAIL)
                .setIssuedAt(new Date(0))
                .setExpiration(new Date(0))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // when & then
        assertThrows(ExpiredJwtException.class, () -> jwtService.extractUsername(expiredToken));
    }

    @Test
    void shouldNotValidateTokenWithDifferentUsername() {
        // given
        String token = jwtService.generateToken(usuario);
        UserDetails differentUser = new User("different@example.com", "password", new ArrayList<>());

        // when
        boolean isValid = jwtService.isTokenValid(token, differentUser);

        // then
        assertFalse(isValid);
    }

    @Test
    void shouldNotValidateExpiredToken() {
        // given
        String expiredToken = Jwts.builder()
                .setSubject(TEST_EMAIL)
                .setIssuedAt(new Date(0))
                .setExpiration(new Date(0))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // when & then
        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(expiredToken, userDetails));
    }

    @Test
    void shouldNotValidateTokenWithInvalidSignature() {
        // given
        String differentSecret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5971";
        Key differentKey = Keys.hmacShaKeyFor(differentSecret.getBytes());
        String tokenWithDifferentSignature = Jwts.builder()
                .setSubject(TEST_EMAIL)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(differentKey, SignatureAlgorithm.HS256)
                .compact();

        // when & then
        assertThrows(SignatureException.class, () -> jwtService.isTokenValid(tokenWithDifferentSignature, userDetails));
    }

    @Test
    void shouldNotValidateTokenWithMalformedJwt() {
        // given
        String malformedToken = "not.a.valid.jwt.token";

        // when & then
        assertThrows(Exception.class, () -> jwtService.isTokenValid(malformedToken, userDetails));
    }

    @Test
    void shouldGenerateTokenWithRole() {
        // when
        String token = jwtService.generateToken(usuario);

        // then
        assertNotNull(token);
        assertTrue(token.length() > 0);
        var claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        assertEquals(TEST_EMAIL, claims.getSubject());
        assertEquals("ADMINISTRADOR", claims.get("role"));
    }

    @Test
    void shouldHandleNullUserDetails() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> jwtService.generateToken(null));
    }

    @Test
    void shouldHandleNullToken() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> jwtService.extractUsername(null));
        assertThrows(IllegalArgumentException.class, () -> jwtService.isTokenValid(null, userDetails));
    }

    @Test
    void shouldHandleNullUserDetailsInValidation() {
        // given
        String token = jwtService.generateToken(usuario);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> jwtService.isTokenValid(token, null));
    }

    @Test
    void shouldThrowExceptionWhenGenerateTokenWithNullUser() {
        // when/then
        assertThrows(IllegalArgumentException.class, () -> jwtService.generateToken(null));
    }

    @Test
    void shouldThrowExceptionWhenExtractUsernameFromNullToken() {
        // when/then
        assertThrows(IllegalArgumentException.class, () -> jwtService.extractUsername(null));
    }

    @Test
    void shouldThrowExceptionWhenValidateNullToken() {
        // when/then
        assertThrows(IllegalArgumentException.class, () -> jwtService.isTokenValid(null, userDetails));
    }

    @Test
    void shouldThrowExceptionWhenValidateWithNullUserDetails() {
        // when/then
        assertThrows(IllegalArgumentException.class, () -> jwtService.isTokenValid(validToken, null));
    }

    @Test
    void shouldReturnFalseWhenUsernameDoesNotMatch() {
        // given
        UserDetails differentUser = new User("different@example.com", "password", new ArrayList<>());

        // when
        boolean isValid = jwtService.isTokenValid(validToken, differentUser);

        // then
        assertFalse(isValid);
    }

    @Test
    void shouldNotValidateTokenWhenTokenIsExpired() {
        // given
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", usuario.getRole().name());
        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setSubject(TEST_EMAIL)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Aguarda o token expirar
        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // when & then
        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(expiredToken, userDetails));
    }

    @Test
    void shouldReturnFalseWhenTokenIsExpired() {
        // given
        JwtService spyService = spy(jwtService);
        String token = spyService.generateToken(usuario);
        doReturn(true).when(spyService).isTokenExpired(token);

        // when
        boolean isValid = spyService.isTokenValid(token, userDetails);

        // then
        assertFalse(isValid);
    }

    @Test
    void shouldValidateTokenWhenUsernameMatchesAndTokenIsNotExpired() {
        // given
        String token = jwtService.generateToken(usuario);

        // when
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // then
        assertTrue(isValid);
    }
} 