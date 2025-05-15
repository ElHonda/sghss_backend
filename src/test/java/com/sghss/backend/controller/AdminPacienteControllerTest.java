package com.sghss.backend.controller;

import com.sghss.backend.dto.ApiResponse;
import com.sghss.backend.dto.PacienteRequestDTO;
import com.sghss.backend.dto.PacienteResponseDTO;
import com.sghss.backend.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AdminPacienteControllerTest {

    @Mock
    private PacienteService pacienteService;

    @InjectMocks
    private AdminPacienteController controller;

    private PacienteRequestDTO requestDTO;
    private PacienteResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestDTO = new PacienteRequestDTO();
        requestDTO.setNome("Paciente Teste");
        requestDTO.setEmail("paciente@teste.com");
        requestDTO.setSenha("senha123");
        requestDTO.setDataNascimento("1990-01-01");
        requestDTO.setCpf("12345678900");
        requestDTO.setTelefone("11999999999");
        requestDTO.setEndereco("Rua Exemplo, 123");

        responseDTO = new PacienteResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setNome("Paciente Teste");
        responseDTO.setEmail("paciente@teste.com");
        responseDTO.setDataNascimento("1990-01-01");
        responseDTO.setCpf("12345678900");
        responseDTO.setTelefone("11999999999");
        responseDTO.setEndereco("Rua Exemplo, 123");
    }

    @Test
    void deveCriarPaciente() {
        when(pacienteService.criar(any(PacienteRequestDTO.class))).thenReturn(responseDTO);
        ResponseEntity<ApiResponse<PacienteResponseDTO>> response = controller.criar(requestDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Paciente criado com sucesso", response.getBody().getMessage());
        assertEquals("Paciente Teste", response.getBody().getData().getNome());
    }

    @Test
    void deveListarTodosPacientes() {
        when(pacienteService.listarTodos()).thenReturn(Arrays.asList(responseDTO));
        ResponseEntity<ApiResponse<List<PacienteResponseDTO>>> response = controller.listarTodos();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void deveBuscarPacientePorIdExistente() {
        when(pacienteService.buscarPorId(1L)).thenReturn(Optional.of(responseDTO));
        ResponseEntity<ApiResponse<PacienteResponseDTO>> response = controller.buscarPorId(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Paciente Teste", response.getBody().getData().getNome());
    }

    @Test
    void deveRetornarNotFoundAoBuscarPacientePorIdInexistente() {
        when(pacienteService.buscarPorId(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<ApiResponse<PacienteResponseDTO>> response = controller.buscarPorId(2L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void deveAtualizarPaciente() {
        when(pacienteService.atualizar(eq(1L), any(PacienteRequestDTO.class))).thenReturn(responseDTO);
        ResponseEntity<ApiResponse<PacienteResponseDTO>> response = controller.atualizar(1L, requestDTO);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Paciente atualizado com sucesso", response.getBody().getMessage());
    }

    @Test
    void deveDeletarPaciente() {
        doNothing().when(pacienteService).deletar(1L);
        ResponseEntity<ApiResponse<Void>> response = controller.deletar(1L);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Paciente deletado com sucesso", response.getBody().getMessage());
    }
} 