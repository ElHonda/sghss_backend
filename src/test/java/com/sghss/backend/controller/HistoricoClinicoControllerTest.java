package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import com.sghss.backend.dto.HistoricoClinicoDTO;
import com.sghss.backend.service.HistoricoClinicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class HistoricoClinicoControllerTest {

    @Mock
    private HistoricoClinicoService historicoClinicoService;

    @InjectMocks
    private HistoricoClinicoController controller;

    private HistoricoClinicoDTO historicoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        historicoDTO = new HistoricoClinicoDTO(1L, "Teste histórico", LocalDateTime.now(), 1L, "Paciente Teste");
    }

    @Test
    void listarPorPaciente_deveRetornarLista() {
        when(historicoClinicoService.listarDTOsPorPaciente(1L)).thenReturn(Arrays.asList(historicoDTO));
        ResponseEntity<ApiResponse<List<HistoricoClinicoDTO>>> response = controller.listarPorPaciente(1L);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getData().size());
        assertEquals("Teste histórico", response.getBody().getData().get(0).getDescricao());
        assertEquals(200, response.getBody().getStatus());
    }

    @Test
    void buscarPorId_existente() {
        when(historicoClinicoService.buscarDTOporId(1L)).thenReturn(Optional.of(historicoDTO));
        ResponseEntity<ApiResponse<HistoricoClinicoDTO>> response = controller.buscarPorId(1L);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Teste histórico", response.getBody().getData().getDescricao());
        assertEquals(200, response.getBody().getStatus());
    }

    @Test
    void buscarPorId_naoExistente() {
        when(historicoClinicoService.buscarDTOporId(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse<HistoricoClinicoDTO>> response = controller.buscarPorId(2L);
        assertEquals(404, response.getStatusCode().value());
    }
} 