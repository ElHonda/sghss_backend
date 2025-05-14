package com.sghss.backend.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private JwtAuthenticationFilter jwtAuthFilter;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private HttpSecurity httpSecurity;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void shouldConfigureSecurityFilterChain() throws Exception {
        // given
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.authenticationProvider(any())).thenReturn(httpSecurity);
        when(httpSecurity.addFilterBefore(any(), any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mock(DefaultSecurityFilterChain.class));

        // when
        SecurityFilterChain filterChain = securityConfig.securityFilterChain(httpSecurity);

        // then
        assertNotNull(filterChain);
        verify(httpSecurity).csrf(any());
        verify(httpSecurity).authorizeHttpRequests(any());
        verify(httpSecurity).sessionManagement(any());
        verify(httpSecurity).authenticationProvider(authenticationProvider);
        verify(httpSecurity).addFilterBefore(jwtAuthFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
    }

    @Test
    void shouldConfigureCors() {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");
        request.setContextPath("");
        request.setServletPath("/test");

        // when
        CorsConfigurationSource corsConfig = securityConfig.corsConfigurationSource();

        // then
        assertNotNull(corsConfig);
        CorsConfiguration config = corsConfig.getCorsConfiguration(request);
        assertNotNull(config);
        assertNotNull(config.getAllowedOrigins());
        assertTrue(config.getAllowedOrigins().contains("*"));
        assertNotNull(config.getAllowedMethods());
        assertTrue(config.getAllowedMethods().contains("GET"));
        assertTrue(config.getAllowedMethods().contains("POST"));
        assertTrue(config.getAllowedMethods().contains("PUT"));
        assertTrue(config.getAllowedMethods().contains("DELETE"));
        assertTrue(config.getAllowedMethods().contains("OPTIONS"));
        assertNotNull(config.getAllowedHeaders());
        assertTrue(config.getAllowedHeaders().contains("*"));
        assertNotNull(config.getExposedHeaders());
        assertTrue(config.getExposedHeaders().contains("Authorization"));
    }

    @Test
    void shouldHandleSecurityConfigurationException() throws Exception {
        // given
        when(httpSecurity.csrf(any())).thenThrow(new RuntimeException("Security configuration error"));

        // when & then
        assertThrows(RuntimeException.class, () -> securityConfig.securityFilterChain(httpSecurity));
    }

    @Test
    void shouldHandleNullHttpSecurity() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> securityConfig.securityFilterChain(null));
    }

    @Test
    void shouldHandleNullAuthenticationProvider() {
        // given
        SecurityConfig config = new SecurityConfig(jwtAuthFilter, null);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> config.securityFilterChain(httpSecurity));
    }

    @Test
    void shouldHandleNullJwtAuthFilter() {
        // given
        SecurityConfig config = new SecurityConfig(null, authenticationProvider);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> config.securityFilterChain(httpSecurity));
    }
} 