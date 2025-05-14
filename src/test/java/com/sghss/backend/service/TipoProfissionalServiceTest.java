package com.sghss.backend.service;

import com.sghss.backend.domain.entity.TipoProfissional;
import com.sghss.backend.dto.TipoProfissionalRequestDTO;
import com.sghss.backend.dto.TipoProfissionalResponseDTO;
import com.sghss.backend.repository.TipoProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoProfissionalServiceTest {

    @Mock
    private TipoProfissionalRepository repository;

    @InjectMocks
    private TipoProfissionalService service;

    private TipoProfissional tipo;
    private TipoProfissionalRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        tipo = new TipoProfissional();
        tipo.setId(1L);
        tipo.setNome("Fisioterapeuta");

        requestDTO = new TipoProfissionalRequestDTO();
        requestDTO.setNome("Fisioterapeuta");
    }

    @Test
    void deveCriarTipoProfissional() {
        when(repository.save(any(TipoProfissional.class))).thenReturn(tipo);
        TipoProfissionalResponseDTO response = service.criar(requestDTO);
        assertNotNull(response);
        assertEquals(tipo.getNome(), response.getNome());
    }

    @Test
    void deveListarTodosTiposProfissional() {
        when(repository.findAll()).thenReturn(Arrays.asList(tipo));
        List<TipoProfissionalResponseDTO> lista = service.listarTodos();
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(tipo.getNome(), lista.get(0).getNome());
    }

    @Test
    void deveBuscarPorIdExistente() {
        when(repository.findById(1L)).thenReturn(Optional.of(tipo));
        Optional<TipoProfissionalResponseDTO> response = service.buscarPorId(1L);
        assertTrue(response.isPresent());
        assertEquals(tipo.getNome(), response.get().getNome());
    }

    @Test
    void deveRetornarVazioAoBuscarPorIdInexistente() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<TipoProfissionalResponseDTO> response = service.buscarPorId(2L);
        assertTrue(response.isEmpty());
    }

    @Test
    void deveAtualizarTipoProfissionalExistente() {
        when(repository.findById(1L)).thenReturn(Optional.of(tipo));
        when(repository.save(any(TipoProfissional.class))).thenReturn(tipo);
        requestDTO.setNome("Nutricionista");
        Optional<TipoProfissionalResponseDTO> response = service.atualizar(1L, requestDTO);
        assertTrue(response.isPresent());
        assertEquals("Nutricionista", response.get().getNome());
    }

    @Test
    void deveRetornarVazioAoAtualizarTipoProfissionalInexistente() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<TipoProfissionalResponseDTO> response = service.atualizar(2L, requestDTO);
        assertTrue(response.isEmpty());
    }

    @Test
    void deveDeletarTipoProfissionalExistente() {
        when(repository.findById(1L)).thenReturn(Optional.of(tipo));
        doNothing().when(repository).delete(tipo);
        boolean deleted = service.deletar(1L);
        assertTrue(deleted);
        verify(repository).delete(tipo);
    }

    @Test
    void naoDeveDeletarTipoProfissionalInexistente() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        boolean deleted = service.deletar(2L);
        assertFalse(deleted);
    }
} 