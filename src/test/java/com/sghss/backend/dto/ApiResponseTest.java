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
        ApiResponse<String> response = ApiResponse.error(400, "Bad Request");

        // then
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertNull(response.getData());
        assertEquals("Bad Request", response.getMessage());
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
    void shouldCreateApiResponseWithNoArgsConstructor() {
        ApiResponse<String> response = new ApiResponse<>();
        
        assertNotNull(response);
        assertEquals(0, response.getStatus());
        assertNull(response.getData());
        assertNull(response.getMessage());
    }

    @Test
    void shouldCreateApiResponseWithAllArgsConstructor() {
        ApiResponse<String> response = new ApiResponse<>(200, "data", "message");
        
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("data", response.getData());
        assertEquals("message", response.getMessage());
    }

    @Test
    void shouldCreateSuccessResponseWithDefaultMessage() {
        ApiResponse<String> response = ApiResponse.success("data");
        
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("data", response.getData());
        assertEquals("Operação realizada com sucesso", response.getMessage());
    }

    @Test
    void shouldUpdateApiResponseFields() {
        ApiResponse<String> response = new ApiResponse<>();
        
        response.setStatus(201);
        response.setData("updated data");
        response.setMessage("updated message");
        
        assertEquals(201, response.getStatus());
        assertEquals("updated data", response.getData());
        assertEquals("updated message", response.getMessage());
    }

    @Test
    void shouldHandleNullDataInSuccessResponse() {
        ApiResponse<String> response = ApiResponse.success(null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertNull(response.getData());
        assertEquals("Operação realizada com sucesso", response.getMessage());
    }

    @Test
    void shouldHandleNullMessageInSuccessResponse() {
        ApiResponse<String> response = ApiResponse.success("data", null);
        
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals("data", response.getData());
        assertNull(response.getMessage());
    }

    @Test
    void shouldHandleDifferentDataTypes() {
        ApiResponse<Integer> intResponse = ApiResponse.success(123);
        assertEquals(123, intResponse.getData());

        ApiResponse<Boolean> boolResponse = ApiResponse.success(true);
        assertTrue(boolResponse.getData());

        ApiResponse<Double> doubleResponse = ApiResponse.success(123.45);
        assertEquals(123.45, doubleResponse.getData());
    }

    @Test
    void shouldHandleComplexDataTypes() {
        TestObject testObject = new TestObject("test", 123);
        ApiResponse<TestObject> response = ApiResponse.success(testObject);
        
        assertNotNull(response);
        assertEquals(200, response.getStatus());
        assertEquals(testObject, response.getData());
        assertEquals("Operação realizada com sucesso", response.getMessage());
    }

    @Test
    void shouldHandleErrorResponseWithDifferentStatusCodes() {
        ApiResponse<?> response400 = ApiResponse.error(400, "Bad Request");
        assertEquals(400, response400.getStatus());

        ApiResponse<?> response401 = ApiResponse.error(401, "Unauthorized");
        assertEquals(401, response401.getStatus());

        ApiResponse<?> response403 = ApiResponse.error(403, "Forbidden");
        assertEquals(403, response403.getStatus());

        ApiResponse<?> response404 = ApiResponse.error(404, "Not Found");
        assertEquals(404, response404.getStatus());

        ApiResponse<?> response500 = ApiResponse.error(500, "Internal Server Error");
        assertEquals(500, response500.getStatus());
    }

    @Test
    void shouldHandleEqualsAndHashCode() {
        ApiResponse<String> response1 = new ApiResponse<>(200, "data", "message");
        ApiResponse<String> response2 = new ApiResponse<>(200, "data", "message");
        ApiResponse<String> response3 = new ApiResponse<>(400, "other", "error");

        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }

    @Test
    void shouldHandleToString() {
        ApiResponse<String> response = new ApiResponse<>(200, "data", "message");
        String toString = response.toString();

        assertTrue(toString.contains("status=200"));
        assertTrue(toString.contains("data=data"));
        assertTrue(toString.contains("message=message"));
    }

    @Test
    void shouldHandleEqualsWithNull() {
        ApiResponse<String> response = new ApiResponse<>(200, "data", "message");
        assertNotEquals(null, response);
    }

    @Test
    void shouldHandleEqualsWithDifferentClass() {
        ApiResponse<String> response = new ApiResponse<>(200, "data", "message");
        assertNotEquals(new Object(), response);
    }

    @Test
    void shouldHandleEqualsWithSameObject() {
        ApiResponse<String> response = new ApiResponse<>(200, "data", "message");
        assertEquals(response, response);
    }

    @Test
    void shouldHandleEqualsWithDifferentStatus() {
        ApiResponse<String> response1 = new ApiResponse<>(200, "data", "message");
        ApiResponse<String> response2 = new ApiResponse<>(400, "data", "message");
        assertNotEquals(response1, response2);
    }

    @Test
    void shouldHandleEqualsWithDifferentData() {
        ApiResponse<String> response1 = new ApiResponse<>(200, "data1", "message");
        ApiResponse<String> response2 = new ApiResponse<>(200, "data2", "message");
        assertNotEquals(response1, response2);
    }

    @Test
    void shouldHandleEqualsWithDifferentMessage() {
        ApiResponse<String> response1 = new ApiResponse<>(200, "data", "message1");
        ApiResponse<String> response2 = new ApiResponse<>(200, "data", "message2");
        assertNotEquals(response1, response2);
    }

    @Test
    void shouldHandleHashCodeWithNullFields() {
        ApiResponse<String> response = new ApiResponse<>();
        assertNotEquals(0, response.hashCode());
    }

    @Test
    void shouldHandleHashCodeConsistency() {
        ApiResponse<String> response = new ApiResponse<>(200, "data", "message");
        int hash1 = response.hashCode();
        int hash2 = response.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void shouldHandleHashCodeWithAllFields() {
        ApiResponse<String> response1 = new ApiResponse<>(200, "data", "message");
        ApiResponse<String> response2 = new ApiResponse<>(200, "data", "message");
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void shouldHandleNullFieldsInEquals() {
        ApiResponse<String> response1 = new ApiResponse<>(200, null, null);
        ApiResponse<String> response2 = new ApiResponse<>(200, null, null);
        assertEquals(response1, response2);
    }

    @Test
    void shouldHandleNullFieldsInHashCode() {
        ApiResponse<String> response1 = new ApiResponse<>(200, null, null);
        ApiResponse<String> response2 = new ApiResponse<>(200, null, null);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    void shouldHandleErrorResponseWithNullMessage() {
        ApiResponse<?> response = ApiResponse.error(400, null);
        assertNotNull(response);
        assertEquals(400, response.getStatus());
        assertNull(response.getData());
        assertNull(response.getMessage());
    }

    private record TestObject(String name, int value) {

    }
} 