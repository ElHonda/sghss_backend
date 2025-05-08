package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PublicControllerTest {

    @InjectMocks
    private PublicController publicController;

    @Test
    void shouldReturnPongForPingEndpoint() {
        // when
        ResponseEntity<ApiResponse<String>> response = publicController.ping();

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        
        ApiResponse<String> body = response.getBody();
        assertNotNull(body);
        assertEquals(200, body.getStatus());
        assertEquals("pong", body.getData());
        assertEquals("Operação realizada com sucesso", body.getMessage());
    }
} 