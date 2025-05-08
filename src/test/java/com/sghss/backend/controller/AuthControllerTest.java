package com.sghss.backend.controller;

import com.sghss.backend.domain.enums.Role;
import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.dto.ApiResponse;
import com.sghss.backend.repository.UsuarioRepository;
import com.sghss.backend.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    private Usuario usuario;
    private AuthController.LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Test User");
        usuario.setEmail("test@example.com");
        usuario.setSenha("encoded_password");
        usuario.setRole(Role.ADMINISTRADOR);

        loginRequest = new AuthController.LoginRequest("test@example.com", "password123");
    }

    @Test
    void shouldRegisterNewUser() {
        // given
        Usuario newUser = new Usuario();
        newUser.setNome("Test User");
        newUser.setEmail("test@example.com");
        newUser.setSenha("password123");
        newUser.setRole(Role.ADMINISTRADOR);

        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // when
        ResponseEntity<ApiResponse<?>> response = authController.register(newUser);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        
        ApiResponse<?> body = response.getBody();
        assertNotNull(body);
        assertEquals(200, body.getStatus());
        assertTrue(body.getMessage().contains("sucesso"));

        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void shouldNotRegisterUserWithExistingEmail() {
        // given
        Usuario newUser = new Usuario();
        newUser.setEmail("test@example.com");
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // when
        ResponseEntity<ApiResponse<?>> response = authController.register(newUser);

        // then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        
        ApiResponse<?> body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertTrue(body.getMessage().contains("já cadastrado"));

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void shouldLoginSuccessfully() {
        // given
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any(Usuario.class))).thenReturn("jwt_token");

        // when
        ResponseEntity<ApiResponse<?>> response = authController.login(loginRequest);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        
        ApiResponse<?> body = response.getBody();
        assertNotNull(body);
        assertEquals(200, body.getStatus());
        assertTrue(body.getMessage().contains("sucesso"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) body.getData();
        assertNotNull(data);
        assertEquals("jwt_token", data.get("token"));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void shouldNotLoginWithInvalidCredentials() {
        // given
        when(authenticationManager.authenticate(any()))
            .thenThrow(new RuntimeException("Invalid credentials"));

        // when
        ResponseEntity<ApiResponse<?>> response = authController.login(loginRequest);

        // then
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        
        ApiResponse<?> body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertTrue(body.getMessage().contains("inválidas"));
        assertNull(body.getData());
    }
} 