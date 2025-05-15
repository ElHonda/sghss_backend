package com.sghss.backend.service;

import com.sghss.backend.domain.entity.Paciente;
import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.domain.enums.Role;
import com.sghss.backend.dto.PacienteRequestDTO;
import com.sghss.backend.dto.PacienteResponseDTO;
import com.sghss.backend.repository.PacienteRepository;
import com.sghss.backend.repository.UsuarioRepository;
import com.sghss.backend.util.CryptoUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PacienteService service;

    private PacienteRequestDTO requestDTO;
    private Paciente paciente;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        requestDTO = new PacienteRequestDTO();
        requestDTO.setNome("Paciente Teste");
        requestDTO.setEmail("paciente@teste.com");
        requestDTO.setSenha("senha123");
        requestDTO.setDataNascimento("1990-01-01");
        requestDTO.setCpf("12345678900");
        requestDTO.setTelefone("11999999999");
        requestDTO.setEndereco("Rua Exemplo, 123");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Paciente Teste");
        usuario.setEmail("paciente@teste.com");
        usuario.setSenha("senha123");
        usuario.setRole(Role.PACIENTE);

        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setUsuario(usuario);
        paciente.setNome("Paciente Teste");
        paciente.setDataNascimento("1990-01-01");
        paciente.setCpf(CryptoUtil.encrypt("12345678900"));
        paciente.setTelefone(CryptoUtil.encrypt("11999999999"));
        paciente.setEndereco(CryptoUtil.encrypt("Rua Exemplo, 123"));
    }

    @Test
    void deveCriarPaciente() {
        when(passwordEncoder.encode(any())).thenReturn("senha123");
        when(usuarioRepository.existsByEmail(any())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);
        PacienteResponseDTO response = service.criar(requestDTO);
        assertNotNull(response);
        assertEquals("Paciente Teste", response.getNome());
        assertEquals("12345678900", response.getCpf());
    }

    @Test
    void deveListarTodosPacientes() {
        when(pacienteRepository.findAll()).thenReturn(Arrays.asList(paciente));
        List<PacienteResponseDTO> lista = service.listarTodos();
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Paciente Teste", lista.get(0).getNome());
    }

    @Test
    void deveBuscarPorIdExistente() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        Optional<PacienteResponseDTO> response = service.buscarPorId(1L);
        assertTrue(response.isPresent());
        assertEquals("Paciente Teste", response.get().getNome());
    }

    @Test
    void deveRetornarVazioAoBuscarPorIdInexistente() {
        when(pacienteRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<PacienteResponseDTO> response = service.buscarPorId(2L);
        assertTrue(response.isEmpty());
    }

    @Test
    void deveAtualizarPacienteExistente() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);
        PacienteRequestDTO dto = new PacienteRequestDTO();
        dto.setNome("Paciente Atualizado");
        dto.setDataNascimento("1991-02-02");
        dto.setCpf("98765432100");
        dto.setTelefone("11888888888");
        dto.setEndereco("Rua Nova, 456");
        PacienteResponseDTO response = service.atualizar(1L, dto);
        assertNotNull(response);
        assertEquals("Paciente Atualizado", response.getNome());
        assertEquals("98765432100", response.getCpf());
    }

    @Test
    void deveDeletarPaciente() {
        doNothing().when(pacienteRepository).deleteById(1L);
        service.deletar(1L);
        verify(pacienteRepository).deleteById(1L);
    }

    @Test
    void deveCriptografarEDescriptografarCamposSensiveis() {
        String original = "teste123";
        String encrypted = CryptoUtil.encrypt(original);
        String decrypted = CryptoUtil.decrypt(encrypted);
        assertNotEquals(original, encrypted);
        assertEquals(original, decrypted);
    }

    @Test
    void deveLancarExcecaoAoCriarPacienteComEmailJaCadastrado() {
        when(usuarioRepository.existsByEmail(any())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.criar(requestDTO));
    }

    @Test
    void deveLancarExcecaoAoAtualizarPacienteInexistente() {
        when(pacienteRepository.findById(anyLong())).thenReturn(Optional.empty());
        PacienteRequestDTO dto = new PacienteRequestDTO();
        dto.setNome("Paciente Atualizado");
        dto.setDataNascimento("1991-02-02");
        dto.setCpf("98765432100");
        dto.setTelefone("11888888888");
        dto.setEndereco("Rua Nova, 456");
        assertThrows(IllegalArgumentException.class, () -> service.atualizar(99L, dto));
    }
} 