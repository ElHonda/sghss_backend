package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import com.sghss.backend.dto.TipoProfissionalRequestDTO;
import com.sghss.backend.dto.TipoProfissionalResponseDTO;
import com.sghss.backend.service.TipoProfissionalService;
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
class AdminTipoProfissionalControllerTest {

    @Mock
    private TipoProfissionalService tipoProfissionalService;

    @InjectMocks
    private AdminTipoProfissionalController controller;

    private TipoProfissionalRequestDTO requestDTO;
    private TipoProfissionalResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new TipoProfissionalRequestDTO();
        requestDTO.setNome("Fisioterapeuta");
        responseDTO = new TipoProfissionalResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNome("Fisioterapeuta");
    }

    @Test
    void deveCriarTipoProfissional() {
        when(tipoProfissionalService.criar(any(TipoProfissionalRequestDTO.class))).thenReturn(responseDTO);
        ResponseEntity<ApiResponse<TipoProfissionalResponseDTO>> response = controller.criar(requestDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Fisioterapeuta", response.getBody().getData().getNome());
    }

    @Test
    void deveListarTodosTiposProfissional() {
        when(tipoProfissionalService.listarTodos()).thenReturn(Arrays.asList(responseDTO));
        ResponseEntity<ApiResponse<List<TipoProfissionalResponseDTO>>> response = controller.listarTodos();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void deveBuscarPorIdExistente() {
        when(tipoProfissionalService.buscarPorId(1L)).thenReturn(Optional.of(responseDTO));
        ResponseEntity<ApiResponse<TipoProfissionalResponseDTO>> response = controller.buscarPorId(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Fisioterapeuta", response.getBody().getData().getNome());
    }

    @Test
    void deveRetornar404AoBuscarPorIdInexistente() {
        when(tipoProfissionalService.buscarPorId(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse<TipoProfissionalResponseDTO>> response = controller.buscarPorId(2L);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    void deveAtualizarTipoProfissionalExistente() {
        when(tipoProfissionalService.atualizar(eq(1L), any(TipoProfissionalRequestDTO.class))).thenReturn(Optional.of(responseDTO));
        ResponseEntity<ApiResponse<TipoProfissionalResponseDTO>> response = controller.atualizar(1L, requestDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Fisioterapeuta", response.getBody().getData().getNome());
    }

    @Test
    void deveRetornar404AoAtualizarTipoProfissionalInexistente() {
        when(tipoProfissionalService.atualizar(eq(2L), any(TipoProfissionalRequestDTO.class))).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse<TipoProfissionalResponseDTO>> response = controller.atualizar(2L, requestDTO);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    void deveDeletarTipoProfissionalExistente() {
        when(tipoProfissionalService.deletar(1L)).thenReturn(true);
        ResponseEntity<ApiResponse<Void>> response = controller.deletar(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().getMessage().contains("deletado"));
    }

    @Test
    void deveRetornar404AoDeletarTipoProfissionalInexistente() {
        when(tipoProfissionalService.deletar(2L)).thenReturn(false);
        ResponseEntity<ApiResponse<Void>> response = controller.deletar(2L);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals(404, response.getBody().getStatus());
    }
} 