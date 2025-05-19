package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import com.sghss.backend.dto.ConsultaRequestDTO;
import com.sghss.backend.dto.ConsultaResponseDTO;
import com.sghss.backend.service.ConsultaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProfissionalConsultaControllerTest {
    @Mock
    private ConsultaService consultaService;
    @InjectMocks
    private ProfissionalConsultaController controller;

    private ConsultaRequestDTO requestDTO;
    private ConsultaResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestDTO = new ConsultaRequestDTO();
        requestDTO.setPacienteId(2L);
        requestDTO.setDataHora(LocalDateTime.now());
        requestDTO.setStatus("AGENDADA");
        requestDTO.setTeleconsulta(false);
        responseDTO = new ConsultaResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setPacienteId(2L);
        responseDTO.setPacienteNome("Paciente Teste");
        responseDTO.setProfissionalId(1L);
        responseDTO.setProfissionalNome("Profissional Teste");
        responseDTO.setDataHora(requestDTO.getDataHora());
        responseDTO.setStatus("AGENDADA");
        responseDTO.setTeleconsulta(false);
    }

    @Test
    void agendarConsulta_deveRetornarSucesso() {
        when(consultaService.criar(anyLong(), any(ConsultaRequestDTO.class))).thenReturn(responseDTO);
        ResponseEntity<ApiResponse<ConsultaResponseDTO>> resp = controller.agendarConsulta(requestDTO, 1L);
        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals(10L, resp.getBody().getData().getId());
        assertEquals("Consulta agendada com sucesso", resp.getBody().getMessage());
    }

    @Test
    void listarConsultas_deveRetornarLista() {
        when(consultaService.listarPorProfissional(1L)).thenReturn(Arrays.asList(responseDTO));
        ResponseEntity<ApiResponse<List<ConsultaResponseDTO>>> resp = controller.listarConsultas(1L);
        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().getData().size());
        assertEquals(10L, resp.getBody().getData().get(0).getId());
    }
} 