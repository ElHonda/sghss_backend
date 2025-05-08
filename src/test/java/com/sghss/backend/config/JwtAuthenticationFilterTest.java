package com.sghss.backend.config;

import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldContinueChainWhenNoAuthHeader() throws ServletException, IOException {
        // given
        when(request.getHeader("Authorization")).thenReturn(null);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractUsername(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void shouldContinueChainWhenInvalidAuthHeader() throws ServletException, IOException {
        // given
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        verify(jwtService, never()).extractUsername(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void shouldAuthenticateWhenValidToken() throws ServletException, IOException {
        // given
        String token = "valid.jwt.token";
        String email = "test@example.com";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(email);
        verify(jwtService).isTokenValid(token, userDetails);
    }

    @Test
    void shouldNotAuthenticateWhenInvalidToken() throws ServletException, IOException {
        // given
        String token = "invalid.jwt.token";
        String email = "test@example.com";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false);

        // when
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(email);
        verify(jwtService).isTokenValid(token, userDetails);
    }

    @Test
    void shouldContinueChainWhenUserNotFound() throws ServletException, IOException {
        // given
        String token = "valid.jwt.token";
        String email = "test@example.com";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userDetailsService.loadUserByUsername(email)).thenThrow(new UsernameNotFoundException("User not found"));

        // when
        assertThrows(UsernameNotFoundException.class, () -> 
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain)
        );

        // then
        verify(jwtService).extractUsername(token);
        verify(userDetailsService).loadUserByUsername(email);
        verify(jwtService, never()).isTokenValid(any(), any());
        verify(filterChain, never()).doFilter(request, response);
    }
} 