package com.sghss.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.sghss.backend.domain.entity.Usuario;

@Configuration
public class AuditorAwareConfig {
    @Bean
    public AuditorAware<Usuario> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }
            Object principal = authentication.getPrincipal();
            if (principal instanceof Usuario usuario) {
                return Optional.of(usuario);
            }
            return Optional.empty();
        };
    }
} 