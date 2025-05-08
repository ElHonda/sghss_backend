package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class AdminControllerTest {

    private final AdminController controller = new AdminController();

    @Test
    void shouldReturnTestMessage() {
        // when
        ResponseEntity<ApiResponse<String>> response = controller.testeAdmin();

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        
        ApiResponse<String> body = response.getBody();
        assertNotNull(body);
        assertEquals(200, body.getStatus());
        assertEquals("Rota de teste para administrador", body.getData());
        assertEquals("Operação realizada com sucesso", body.getMessage());
    }
} 