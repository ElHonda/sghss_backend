package com.sghss.backend.service;

import com.sghss.backend.domain.entity.HistoricoClinico;
import com.sghss.backend.domain.entity.Paciente;
import com.sghss.backend.dto.HistoricoClinicoDTO;
import com.sghss.backend.repository.HistoricoClinicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoricoClinicoServiceTest {
    @Mock
    private HistoricoClinicoRepository historicoClinicoRepository;

    @InjectMocks
    private HistoricoClinicoService historicoClinicoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarPorPaciente_deveRetornarLista() {
        HistoricoClinico h1 = new HistoricoClinico();
        HistoricoClinico h2 = new HistoricoClinico();
        when(historicoClinicoRepository.findByPacienteId(1L)).thenReturn(Arrays.asList(h1, h2));
        List<HistoricoClinico> result = historicoClinicoService.listarPorPaciente(1L);
        assertEquals(2, result.size());
        verify(historicoClinicoRepository, times(1)).findByPacienteId(1L);
    }

    @Test
    void buscarPorId_existente() {
        HistoricoClinico h = new HistoricoClinico();
        when(historicoClinicoRepository.findById(1L)).thenReturn(Optional.of(h));
        Optional<HistoricoClinico> result = historicoClinicoService.buscarPorId(1L);
        assertTrue(result.isPresent());
        verify(historicoClinicoRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPorId_naoExistente() {
        when(historicoClinicoRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<HistoricoClinico> result = historicoClinicoService.buscarPorId(2L);
        assertFalse(result.isPresent());
        verify(historicoClinicoRepository, times(1)).findById(2L);
    }

    @Test
    void listarDTOsPorPaciente_deveRetornarListaDTO() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("Paciente Teste");
        HistoricoClinico h = new HistoricoClinico();
        h.setId(1L);
        h.setDescricao("Descricao Teste");
        h.setData(LocalDateTime.now());
        h.setPaciente(paciente);
        when(historicoClinicoRepository.findByPacienteId(1L)).thenReturn(Arrays.asList(h));
        List<HistoricoClinicoDTO> dtos = historicoClinicoService.listarDTOsPorPaciente(1L);
        assertEquals(1, dtos.size());
        assertEquals("Descricao Teste", dtos.get(0).getDescricao());
        assertEquals(1L, dtos.get(0).getPacienteId());
        assertEquals("Paciente Teste", dtos.get(0).getPacienteNome());
    }

    @Test
    void buscarDTOporId_existente() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("Paciente Teste");
        HistoricoClinico h = new HistoricoClinico();
        h.setId(1L);
        h.setDescricao("Descricao Teste");
        h.setData(LocalDateTime.now());
        h.setPaciente(paciente);
        when(historicoClinicoRepository.findById(1L)).thenReturn(Optional.of(h));
        Optional<HistoricoClinicoDTO> dto = historicoClinicoService.buscarDTOporId(1L);
        assertTrue(dto.isPresent());
        assertEquals("Descricao Teste", dto.get().getDescricao());
        assertEquals(1L, dto.get().getPacienteId());
        assertEquals("Paciente Teste", dto.get().getPacienteNome());
    }

    @Test
    void buscarDTOporId_naoExistente() {
        when(historicoClinicoRepository.findById(2L)).thenReturn(Optional.empty());
        Optional<HistoricoClinicoDTO> dto = historicoClinicoService.buscarDTOporId(2L);
        assertFalse(dto.isPresent());
    }

    @Test
    void listarDTOsPorPaciente_pacienteNull() {
        HistoricoClinico h = new HistoricoClinico();
        h.setId(2L);
        h.setDescricao("Sem paciente");
        h.setData(LocalDateTime.now());
        h.setPaciente(null);
        when(historicoClinicoRepository.findByPacienteId(99L)).thenReturn(Arrays.asList(h));
        List<HistoricoClinicoDTO> dtos = historicoClinicoService.listarDTOsPorPaciente(99L);
        assertEquals(1, dtos.size());
        assertNull(dtos.get(0).getPacienteId());
        assertNull(dtos.get(0).getPacienteNome());
    }

    @Test
    void buscarDTOporId_pacienteNull() {
        HistoricoClinico h = new HistoricoClinico();
        h.setId(2L);
        h.setDescricao("Sem paciente");
        h.setData(LocalDateTime.now());
        h.setPaciente(null);
        when(historicoClinicoRepository.findById(99L)).thenReturn(Optional.of(h));
        Optional<HistoricoClinicoDTO> dto = historicoClinicoService.buscarDTOporId(99L);
        assertTrue(dto.isPresent());
        assertNull(dto.get().getPacienteId());
        assertNull(dto.get().getPacienteNome());
    }
} 