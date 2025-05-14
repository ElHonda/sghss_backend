package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import com.sghss.backend.dto.ProfissionalRequestDTO;
import com.sghss.backend.dto.ProfissionalResponseDTO;
import com.sghss.backend.service.ProfissionalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminProfissionalControllerTest {

    @Mock
    private ProfissionalService profissionalService;

    @InjectMocks
    private AdminProfissionalController controller;

    private ProfissionalRequestDTO requestDTO;
    private ProfissionalResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new ProfissionalRequestDTO();
        requestDTO.setNome("Profissional Teste");
        requestDTO.setEmail("profissional@teste.com");
        requestDTO.setSenha("senha123");
        requestDTO.setTipoProfissionalId(1L);
        requestDTO.setRegistroProfissional("12345");

        responseDTO = new ProfissionalResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setUsuarioId(1L);
        responseDTO.setNomeUsuario("Profissional Teste");
        responseDTO.setEmailUsuario("profissional@teste.com");
        responseDTO.setTipoProfissionalId(1L);
        responseDTO.setTipoProfissionalNome("Fisioterapeuta");
        responseDTO.setRegistroProfissional("12345");
    }

    @Test
    void deveCriarProfissional() {
        when(profissionalService.criar(any(ProfissionalRequestDTO.class))).thenReturn(responseDTO);
        ResponseEntity<ApiResponse<ProfissionalResponseDTO>> response = controller.criar(requestDTO);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Profissional Teste", response.getBody().getData().getNomeUsuario());
    }

    @Test
    void deveListarTodosProfissionais() {
        when(profissionalService.listarTodos()).thenReturn(Arrays.asList(responseDTO));
        ResponseEntity<ApiResponse<List<ProfissionalResponseDTO>>> response = controller.listarTodos();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void deveBuscarPorIdExistente() {
        when(profissionalService.buscarPorId(1L)).thenReturn(Optional.of(responseDTO));
        ResponseEntity<ApiResponse<ProfissionalResponseDTO>> response = controller.buscarPorId(1L);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Profissional Teste", response.getBody().getData().getNomeUsuario());
    }

    @Test
    void deveRetornar404AoBuscarPorIdInexistente() {
        when(profissionalService.buscarPorId(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse<ProfissionalResponseDTO>> response = controller.buscarPorId(2L);
        assertEquals(404, response.getStatusCode().value());
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    void deveAtualizarProfissionalExistente() {
        when(profissionalService.atualizar(eq(1L), any(ProfissionalRequestDTO.class))).thenReturn(Optional.of(responseDTO));
        ResponseEntity<ApiResponse<ProfissionalResponseDTO>> response = controller.atualizar(1L, requestDTO);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Profissional Teste", response.getBody().getData().getNomeUsuario());
    }

    @Test
    void deveRetornar404AoAtualizarProfissionalInexistente() {
        when(profissionalService.atualizar(eq(2L), any(ProfissionalRequestDTO.class))).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse<ProfissionalResponseDTO>> response = controller.atualizar(2L, requestDTO);
        assertEquals(404, response.getStatusCode().value());
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    void deveDeletarProfissionalExistente() {
        when(profissionalService.deletar(1L)).thenReturn(true);
        ResponseEntity<ApiResponse<Void>> response = controller.deletar(1L);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().getMessage().contains("deletado"));
    }

    @Test
    void deveRetornar404AoDeletarProfissionalInexistente() {
        when(profissionalService.deletar(2L)).thenReturn(false);
        ResponseEntity<ApiResponse<Void>> response = controller.deletar(2L);
        assertEquals(404, response.getStatusCode().value());
        assertEquals(404, response.getBody().getStatus());
    }
} 