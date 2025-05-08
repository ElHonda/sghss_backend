package com.sghss.backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtConfigTest {

    @Test
    void shouldGetSecret() {
        // given
        JwtConfig jwtConfig = new JwtConfig();
        String expectedSecret = "test_secret";
        ReflectionTestUtils.setField(jwtConfig, "secret", expectedSecret);

        // when
        String secret = jwtConfig.getSecret();

        // then
        assertEquals(expectedSecret, secret);
    }

    @Test
    void shouldGetExpiration() {
        // given
        JwtConfig jwtConfig = new JwtConfig();
        Long expectedExpiration = 86400000L;
        ReflectionTestUtils.setField(jwtConfig, "expiration", expectedExpiration);

        // when
        Long expiration = jwtConfig.getExpiration();

        // then
        assertEquals(expectedExpiration, expiration);
    }
} 