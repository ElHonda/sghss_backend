package com.sghss.backend.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Nested
    @DisplayName("Testes de Resposta de Sucesso")
    class SuccessResponseTests {

        @Test
        @DisplayName("Deve criar resposta de sucesso com dados")
        void shouldCreateSuccessResponseWithData() {
            // given
            Map<String, Object> data = new HashMap<>();
            data.put("id", 1);
            data.put("nome", "Teste");

            // when
            ApiResponse<Map<String, Object>> response = ApiResponse.success(data);

            // then
            assertNotNull(response);
            assertEquals(200, response.getStatus());
            assertEquals("Operação realizada com sucesso", response.getMessage());
            assertEquals(data, response.getData());
        }

        @Test
        @DisplayName("Deve criar resposta de sucesso com dados e mensagem personalizada")
        void shouldCreateSuccessResponseWithDataAndCustomMessage() {
            // given
            Map<String, Object> data = new HashMap<>();
            data.put("id", 1);
            data.put("nome", "Teste");
            String message = "Operação realizada com sucesso";

            // when
            ApiResponse<Map<String, Object>> response = ApiResponse.success(data, message);

            // then
            assertNotNull(response);
            assertEquals(200, response.getStatus());
            assertEquals(message, response.getMessage());
            assertEquals(data, response.getData());
        }

        @Test
        @DisplayName("Deve criar resposta de sucesso com dados nulos")
        void shouldCreateSuccessResponseWithNullData() {
            // when
            ApiResponse<Map<String, Object>> response = ApiResponse.success(null);

            // then
            assertNotNull(response);
            assertEquals(200, response.getStatus());
            assertEquals("Operação realizada com sucesso", response.getMessage());
            assertNull(response.getData());
        }

        @Test
        @DisplayName("Deve criar resposta de sucesso com dados nulos e mensagem personalizada")
        void shouldCreateSuccessResponseWithNullDataAndCustomMessage() {
            // given
            String message = "Operação realizada com sucesso";

            // when
            ApiResponse<Map<String, Object>> response = ApiResponse.success(null, message);

            // then
            assertNotNull(response);
            assertEquals(200, response.getStatus());
            assertEquals(message, response.getMessage());
            assertNull(response.getData());
        }
    }

    @Nested
    @DisplayName("Testes de Resposta de Erro")
    class ErrorResponseTests {

        @Test
        @DisplayName("Deve criar resposta de erro")
        void shouldCreateErrorResponse() {
            // given
            int status = 400;
            String message = "Erro na operação";

            // when
            ApiResponse<Void> response = ApiResponse.error(status, message);

            // then
            assertNotNull(response);
            assertEquals(status, response.getStatus());
            assertEquals(message, response.getMessage());
            assertNull(response.getData());
        }

        @Test
        @DisplayName("Deve criar resposta de erro com status negativo")
        void shouldCreateErrorResponseWithNegativeStatus() {
            // given
            int status = -1;
            String message = "Erro na operação";

            // when
            ApiResponse<Void> response = ApiResponse.error(status, message);

            // then
            assertNotNull(response);
            assertEquals(status, response.getStatus());
            assertEquals(message, response.getMessage());
            assertNull(response.getData());
        }

        @ParameterizedTest
        @ValueSource(ints = {400, 401, 403, 404, 500})
        @DisplayName("Deve criar resposta de erro com diferentes códigos de status")
        void shouldCreateErrorResponseWithDifferentStatusCodes(int status) {
            // given
            String message = "Error message";

            // when
            ApiResponse<Void> response = ApiResponse.error(status, message);

            // then
            assertNotNull(response);
            assertEquals(status, response.getStatus());
            assertEquals(message, response.getMessage());
            assertNull(response.getData());
        }
    }

    @Nested
    @DisplayName("Testes de Construtor")
    class ConstructorTests {

        @Test
        @DisplayName("Deve criar resposta com construtor padrão")
        void shouldCreateResponseWithDefaultConstructor() {
            // when
            ApiResponse<String> response = new ApiResponse<>();

            // then
            assertNotNull(response);
            assertEquals(0, response.getStatus());
            assertNull(response.getData());
            assertNull(response.getMessage());
        }

        @Test
        @DisplayName("Deve criar resposta com construtor completo")
        void shouldCreateResponseWithAllArgsConstructor() {
            // given
            int status = 200;
            String data = "Test data";
            String message = "Test message";

            // when
            ApiResponse<String> response = new ApiResponse<>(status, data, message);

            // then
            assertNotNull(response);
            assertEquals(status, response.getStatus());
            assertEquals(data, response.getData());
            assertEquals(message, response.getMessage());
        }
    }

    @Nested
    @DisplayName("Testes de Equals e HashCode")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Deve comparar corretamente objetos iguais")
        void shouldCompareEqualObjects() {
            // given
            ApiResponse<String> response1 = new ApiResponse<>(200, "data", "message");
            ApiResponse<String> response2 = new ApiResponse<>(200, "data", "message");

            // then
            assertEquals(response1, response2);
            assertEquals(response1.hashCode(), response2.hashCode());
        }

        @Test
        @DisplayName("Deve comparar corretamente objetos diferentes")
        void shouldCompareDifferentObjects() {
            // given
            ApiResponse<String> response1 = new ApiResponse<>(200, "data1", "message");
            ApiResponse<String> response2 = new ApiResponse<>(200, "data2", "message");
            ApiResponse<String> response3 = new ApiResponse<>(400, "data1", "message");

            // then
            assertNotEquals(response1, response2);
            assertNotEquals(response1, response3);
            assertNotEquals(response1.hashCode(), response2.hashCode());
            assertNotEquals(response1.hashCode(), response3.hashCode());
        }

        @Test
        @DisplayName("Deve comparar corretamente com null")
        void shouldCompareWithNull() {
            // given
            ApiResponse<String> response = new ApiResponse<>(200, "data", "message");

            // then
            assertNotEquals(null, response);
        }

        @Test
        @DisplayName("Deve comparar corretamente com objeto de classe diferente")
        void shouldCompareWithDifferentClass() {
            // given
            ApiResponse<String> response = new ApiResponse<>(200, "data", "message");

            // then
            assertNotEquals(new Object(), response);
        }
    }

    @Nested
    @DisplayName("Testes de ToString")
    class ToStringTests {

        @Test
        @DisplayName("Deve gerar string correta")
        void shouldGenerateCorrectString() {
            // given
            ApiResponse<String> response = new ApiResponse<>(200, "data", "message");

            // when
            String toString = response.toString();

            // then
            assertTrue(toString.contains("status=200"));
            assertTrue(toString.contains("data=data"));
            assertTrue(toString.contains("message=message"));
        }

        @Test
        @DisplayName("Deve gerar string correta com campos nulos")
        void shouldGenerateCorrectStringWithNullFields() {
            // given
            ApiResponse<String> response = new ApiResponse<>(200, null, null);

            // when
            String toString = response.toString();

            // then
            assertTrue(toString.contains("status=200"));
            assertTrue(toString.contains("data=null"));
            assertTrue(toString.contains("message=null"));
        }
    }
} 