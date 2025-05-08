package com.sghss.backend.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void shouldCreateApiResponseWithAllFields() {
        // given
        int status = 200;
        String message = "Success";
        String data = "Test data";

        // when
        ApiResponse<String> response = new ApiResponse<>(status, data, message);

        // then
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
    }

    @Test
    void shouldCreateApiResponseWithNullData() {
        // given
        int status = 404;
        String message = "Not found";

        // when
        ApiResponse<String> response = new ApiResponse<>(status, null, message);

        // then
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void shouldCreateSuccessResponse() {
        // when
        ApiResponse<String> response = ApiResponse.success("data");

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("data", response.getData());
        assertEquals("Operação realizada com sucesso", response.getMessage());
    }

    @Test
    void shouldCreateSuccessResponseWithCustomMessage() {
        // when
        ApiResponse<String> response = ApiResponse.success("data", "Custom message");

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("data", response.getData());
        assertEquals("Custom message", response.getMessage());
    }

    @Test
    void shouldCreateErrorResponse() {
        // when
        ApiResponse<String> response = ApiResponse.error(400, "Error message");

        // then
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertNull(response.getData());
        assertEquals("Error message", response.getMessage());
    }

    @Test
    void shouldCreateResponseWithAllArgsConstructor() {
        // when
        ApiResponse<String> response = new ApiResponse<>(200, "data", "message");

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("data", response.getData());
        assertEquals("message", response.getMessage());
    }

    @Test
    void shouldCreateResponseWithNoArgsConstructor() {
        // when
        ApiResponse<String> response = new ApiResponse<>();

        // then
        assertNotNull(response);
        assertEquals(0, response.getStatus());
        assertNull(response.getData());
        assertNull(response.getMessage());
    }
} 