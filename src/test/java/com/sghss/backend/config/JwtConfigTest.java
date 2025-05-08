package com.sghss.backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtConfigTest {

    private final JwtConfig jwtConfig = new JwtConfig();

    @Test
    void shouldGetSecret() {
        // given
        String expectedSecret = "test_secret";
        ReflectionTestUtils.setField(jwtConfig, "secret", expectedSecret);

        // when
        String actualSecret = jwtConfig.getSecret();

        // then
        assertEquals(expectedSecret, actualSecret);
    }

    @Test
    void shouldGetExpiration() {
        // given
        Long expectedExpiration = 86400000L; // 24 hours
        ReflectionTestUtils.setField(jwtConfig, "expiration", expectedExpiration);

        // when
        Long actualExpiration = jwtConfig.getExpiration();

        // then
        assertEquals(expectedExpiration, actualExpiration);
    }

    @Test
    void shouldHandleNullSecret() {
        // given
        ReflectionTestUtils.setField(jwtConfig, "secret", null);

        // when
        String secret = jwtConfig.getSecret();

        // then
        assertNull(secret);
    }

    @Test
    void shouldHandleNullExpiration() {
        // given
        ReflectionTestUtils.setField(jwtConfig, "expiration", null);

        // when
        Long expiration = jwtConfig.getExpiration();

        // then
        assertNull(expiration);
    }

    @Test
    void shouldHandleEmptySecret() {
        // given
        ReflectionTestUtils.setField(jwtConfig, "secret", "");

        // when
        String secret = jwtConfig.getSecret();

        // then
        assertEquals("", secret);
    }

    @Test
    void shouldHandleZeroExpiration() {
        // given
        ReflectionTestUtils.setField(jwtConfig, "expiration", 0L);

        // when
        Long expiration = jwtConfig.getExpiration();

        // then
        assertEquals(0L, expiration);
    }
} 