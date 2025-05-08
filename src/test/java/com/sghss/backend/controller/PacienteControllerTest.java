package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class PacienteControllerTest {

    private final PacienteController controller = new PacienteController();

    @Test
    void shouldReturnTestMessage() {
        // when
        ResponseEntity<ApiResponse<String>> response = controller.testePaciente();

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        
        ApiResponse<String> body = response.getBody();
        assertNotNull(body);
        assertEquals(200, body.getStatus());
        assertEquals("Rota de teste para paciente", body.getData());
        assertEquals("Operação realizada com sucesso", body.getMessage());
    }
} 