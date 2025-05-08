package com.sghss.backend.controller;

import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.domain.enums.Role;
import com.sghss.backend.dto.ApiResponse;
import com.sghss.backend.repository.UsuarioRepository;
import com.sghss.backend.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Nested
    @DisplayName("Testes de Registro de Usuário")
    class RegisterTests {

        @Test
        @DisplayName("Deve registrar um novo usuário com sucesso")
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
        @DisplayName("Não deve registrar usuário com email já existente")
        void shouldNotRegisterUserWithExistingEmail() {
            // given
            Usuario newUser = new Usuario();
            newUser.setNome("Test User");
            newUser.setEmail("test@example.com");
            newUser.setSenha("password123");
            newUser.setRole(Role.ADMINISTRADOR);

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
        @DisplayName("Não deve registrar usuário com role inválida")
        void shouldNotRegisterUserWithInvalidRole() {
            // given
            Usuario newUser = new Usuario();
            newUser.setNome("Test User");
            newUser.setEmail("test@example.com");
            newUser.setSenha("password123");
            newUser.setRole(null);

            // when
            ResponseEntity<ApiResponse<?>> response = authController.register(newUser);

            // then
            assertNotNull(response);
            assertEquals(400, response.getStatusCode().value());
            
            ApiResponse<?> body = response.getBody();
            assertNotNull(body);
            assertEquals(400, body.getStatus());
            assertTrue(body.getMessage().contains("inválido"));

            verify(usuarioRepository, never()).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Não deve registrar com usuário nulo")
        void shouldNotRegisterWithNullUser() {
            // when
            ResponseEntity<ApiResponse<?>> response = authController.register(null);

            // then
            assertNotNull(response);
            assertEquals(400, response.getStatusCode().value());
            
            ApiResponse<?> body = response.getBody();
            assertNotNull(body);
            assertEquals(400, body.getStatus());
            assertTrue(body.getMessage().contains("inválido"));

            verify(usuarioRepository, never()).save(any(Usuario.class));
        }

        @Test
        @DisplayName("Não deve registrar com campos inválidos")
        void shouldNotRegisterWithInvalidFields() {
            // given
            Usuario[] invalidUsers = {
                createInvalidUser(null, null, null, null),
                createInvalidUser("", "", "", Role.ADMINISTRADOR),
                createInvalidUser("   ", "   ", "   ", Role.ADMINISTRADOR),
                createInvalidUser("Test", null, "password", null),
                createInvalidUser(null, "test@example.com", null, Role.ADMINISTRADOR)
            };

            for (Usuario invalidUser : invalidUsers) {
                // when
                ResponseEntity<ApiResponse<?>> response = authController.register(invalidUser);

                // then
                assertNotNull(response);
                assertEquals(400, response.getStatusCode().value());
                
                ApiResponse<?> body = response.getBody();
                assertNotNull(body);
                assertEquals(400, body.getStatus());
                assertTrue(body.getMessage().contains("inválido"));

                verify(usuarioRepository, never()).save(any(Usuario.class));
            }
        }
    }

    @Nested
    @DisplayName("Testes de Login")
    class LoginTests {

        @Test
        @DisplayName("Deve fazer login com sucesso")
        void shouldLoginSuccessfully() {
            // given
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(null);
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

            Map<String, Object> data = (Map<String, Object>) body.getData();
            assertNotNull(data);
            assertEquals("jwt_token", data.get("token"));
        }

        @Test
        @DisplayName("Não deve fazer login com credenciais inválidas")
        void shouldNotLoginWithInvalidCredentials() {
            // given
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Credenciais inválidas"));

            // when
            ResponseEntity<ApiResponse<?>> response = authController.login(loginRequest);

            // then
            assertNotNull(response);
            assertEquals(401, response.getStatusCode().value());
            
            ApiResponse<?> body = response.getBody();
            assertNotNull(body);
            assertEquals(401, body.getStatus());
            assertTrue(body.getMessage().contains("Credenciais inválidas"));
        }

        @Test
        @DisplayName("Não deve fazer login com usuário inexistente")
        void shouldNotLoginWithNonexistentUser() {
            // given
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(null);
            when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

            // when
            ResponseEntity<ApiResponse<?>> response = authController.login(loginRequest);

            // then
            assertNotNull(response);
            assertEquals(404, response.getStatusCode().value());
            
            ApiResponse<?> body = response.getBody();
            assertNotNull(body);
            assertEquals(404, body.getStatus());
            assertTrue(body.getMessage().contains("não encontrado"));
        }

        @Test
        @DisplayName("Não deve fazer login com requisição nula")
        void shouldNotLoginWithNullRequest() {
            // when
            ResponseEntity<ApiResponse<?>> response = authController.login(null);

            // then
            assertNotNull(response);
            assertEquals(400, response.getStatusCode().value());
            
            ApiResponse<?> body = response.getBody();
            assertNotNull(body);
            assertEquals(400, body.getStatus());
            assertTrue(body.getMessage().contains("inválidas"));
        }

        @Test
        @DisplayName("Não deve fazer login com campos inválidos")
        void shouldNotLoginWithInvalidFields() {
            // given
            AuthController.LoginRequest[] invalidRequests = {
                new AuthController.LoginRequest(null, null),
                new AuthController.LoginRequest("", ""),
                new AuthController.LoginRequest("   ", "   "),
                new AuthController.LoginRequest("test@example.com", null),
                new AuthController.LoginRequest(null, "password123")
            };

            for (AuthController.LoginRequest invalidRequest : invalidRequests) {
                // when
                ResponseEntity<ApiResponse<?>> response = authController.login(invalidRequest);

                // then
                assertNotNull(response);
                assertEquals(400, response.getStatusCode().value());
                
                ApiResponse<?> body = response.getBody();
                assertNotNull(body);
                assertEquals(400, body.getStatus());
                assertTrue(body.getMessage().contains("inválidas"));
            }
        }
    }

    private Usuario createInvalidUser(String nome, String email, String senha, Role role) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setRole(role);
        return usuario;
    }
} 