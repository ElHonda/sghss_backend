package com.sghss.backend.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void shouldHaveCorrectRoles() {
        // when
        Role[] roles = Role.values();

        // then
        assertEquals(3, roles.length);
        assertEquals(Role.ADMINISTRADOR, roles[0]);
        assertEquals(Role.PROFISSIONAL, roles[1]);
        assertEquals(Role.PACIENTE, roles[2]);
    }

    @Test
    void shouldGetRoleByName() {
        // when
        Role admin = Role.valueOf("ADMINISTRADOR");
        Role profissional = Role.valueOf("PROFISSIONAL");
        Role paciente = Role.valueOf("PACIENTE");

        // then
        assertEquals(Role.ADMINISTRADOR, admin);
        assertEquals(Role.PROFISSIONAL, profissional);
        assertEquals(Role.PACIENTE, paciente);
    }

    @Test
    void shouldThrowExceptionForInvalidRole() {
        // when & then
        assertThrows(IllegalArgumentException.class, () -> Role.valueOf("INVALID_ROLE"));
    }
} 