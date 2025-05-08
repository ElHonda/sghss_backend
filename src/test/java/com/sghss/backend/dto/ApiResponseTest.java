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
        // given
        String data = "Success data";
        String message = "Operation successful";

        // when
        ApiResponse<String> response = ApiResponse.success(data, message);

        // then
        assertEquals(200, response.getStatus());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
    }

    @Test
    void shouldCreateErrorResponse() {
        // given
        String message = "Error occurred";
        int status = 400;

        // when
        ApiResponse<Void> response = ApiResponse.error(status, message);

        // then
        assertEquals(status, response.getStatus());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
    }
} 