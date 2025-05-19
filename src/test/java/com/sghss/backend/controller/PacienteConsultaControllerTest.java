package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
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
import static org.mockito.Mockito.*;

class PacienteConsultaControllerTest {
    @Mock
    private ConsultaService consultaService;
    @InjectMocks
    private PacienteConsultaController controller;

    private ConsultaResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        responseDTO = new ConsultaResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setPacienteId(2L);
        responseDTO.setPacienteNome("Paciente Teste");
        responseDTO.setProfissionalId(1L);
        responseDTO.setProfissionalNome("Profissional Teste");
        responseDTO.setDataHora(LocalDateTime.now());
        responseDTO.setStatus("AGENDADA");
        responseDTO.setTeleconsulta(false);
    }

    @Test
    void listarConsultas_deveRetornarLista() {
        when(consultaService.listarPorPaciente(2L)).thenReturn(Arrays.asList(responseDTO));
        ResponseEntity<ApiResponse<List<ConsultaResponseDTO>>> resp = controller.listarConsultas(2L);
        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals(1, resp.getBody().getData().size());
        assertEquals(10L, resp.getBody().getData().get(0).getId());
    }
} 