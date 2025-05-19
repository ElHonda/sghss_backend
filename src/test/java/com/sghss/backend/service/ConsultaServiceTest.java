package com.sghss.backend.service;

import com.sghss.backend.domain.entity.Consulta;
import com.sghss.backend.domain.entity.Paciente;
import com.sghss.backend.domain.entity.Profissional;
import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.dto.ConsultaRequestDTO;
import com.sghss.backend.dto.ConsultaResponseDTO;
import com.sghss.backend.repository.ConsultaRepository;
import com.sghss.backend.repository.PacienteRepository;
import com.sghss.backend.repository.ProfissionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConsultaServiceTest {
    @Mock
    private ConsultaRepository consultaRepository;
    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private ProfissionalRepository profissionalRepository;
    @InjectMocks
    private ConsultaService consultaService;

    private Profissional profissional;
    private Paciente paciente;
    private Consulta consulta;
    private ConsultaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        profissional = new Profissional();
        profissional.setId(1L);
        Usuario usuarioProf = new Usuario();
        usuarioProf.setId(1L);
        usuarioProf.setNome("Profissional Teste");
        profissional.setUsuario(usuarioProf);
        paciente = new Paciente();
        paciente.setId(2L);
        paciente.setNome("Paciente Teste");
        Usuario usuarioPaciente = new Usuario();
        usuarioPaciente.setId(2L);
        usuarioPaciente.setNome("Paciente Teste");
        paciente.setUsuario(usuarioPaciente);
        consulta = new Consulta();
        consulta.setId(10L);
        consulta.setProfissional(profissional);
        consulta.setPaciente(paciente);
        consulta.setDataHora(LocalDateTime.now());
        consulta.setStatus("AGENDADA");
        consulta.setTeleconsulta(false);
        consulta.setVideochamadaUrl(null);
        requestDTO = new ConsultaRequestDTO();
        requestDTO.setPacienteId(2L);
        requestDTO.setDataHora(consulta.getDataHora());
        requestDTO.setStatus("AGENDADA");
        requestDTO.setTeleconsulta(false);
        requestDTO.setVideochamadaUrl(null);
    }

    @Test
    void criar_deveAgendarConsulta() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(pacienteRepository.findById(2L)).thenReturn(Optional.of(paciente));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);
        ConsultaResponseDTO dto = consultaService.criar(1L, requestDTO);
        assertNotNull(dto);
        assertEquals(10L, dto.getId());
        assertEquals(2L, dto.getPacienteId());
        assertEquals("Paciente Teste", dto.getPacienteNome());
        assertEquals(1L, dto.getProfissionalId());
        assertEquals("AGENDADA", dto.getStatus());
    }

    @Test
    void criar_deveLancarExcecaoSeProfissionalNaoEncontrado() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> consultaService.criar(1L, requestDTO));
    }

    @Test
    void criar_deveLancarExcecaoSePacienteNaoEncontrado() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(pacienteRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> consultaService.criar(1L, requestDTO));
    }

    @Test
    void listarPorProfissional_deveRetornarConsultas() {
        when(consultaRepository.findByProfissionalId(1L)).thenReturn(Arrays.asList(consulta));
        List<ConsultaResponseDTO> lista = consultaService.listarPorProfissional(1L);
        assertEquals(1, lista.size());
        assertEquals(10L, lista.get(0).getId());
    }

    @Test
    void listarPorPaciente_deveRetornarConsultas() {
        when(consultaRepository.findByPacienteId(2L)).thenReturn(Arrays.asList(consulta));
        List<ConsultaResponseDTO> lista = consultaService.listarPorPaciente(2L);
        assertEquals(1, lista.size());
        assertEquals(10L, lista.get(0).getId());
    }

    @Test
    void buscarPorId_existente() {
        when(consultaRepository.findById(10L)).thenReturn(Optional.of(consulta));
        Optional<ConsultaResponseDTO> dto = consultaService.buscarPorId(10L);
        assertTrue(dto.isPresent());
        assertEquals(10L, dto.get().getId());
    }

    @Test
    void buscarPorId_naoExistente() {
        when(consultaRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<ConsultaResponseDTO> dto = consultaService.buscarPorId(99L);
        assertFalse(dto.isPresent());
    }

    @Test
    void toResponseDTO_deveRetornarDTOComPacienteNulo() {
        Consulta consultaSemPaciente = new Consulta();
        consultaSemPaciente.setId(20L);
        consultaSemPaciente.setPaciente(null);
        consultaSemPaciente.setProfissional(profissional);
        consultaSemPaciente.setDataHora(LocalDateTime.now());
        consultaSemPaciente.setStatus("AGENDADA");
        consultaSemPaciente.setTeleconsulta(false);
        consultaSemPaciente.setVideochamadaUrl(null);
        ConsultaResponseDTO dto = invokeToResponseDTO(consultaSemPaciente);
        assertEquals(20L, dto.getId());
        assertNull(dto.getPacienteId());
        assertNull(dto.getPacienteNome());
    }

    @Test
    void toResponseDTO_deveRetornarDTOComProfissionalNulo() {
        Consulta consultaSemProf = new Consulta();
        consultaSemProf.setId(21L);
        consultaSemProf.setPaciente(paciente);
        consultaSemProf.setProfissional(null);
        consultaSemProf.setDataHora(LocalDateTime.now());
        consultaSemProf.setStatus("AGENDADA");
        consultaSemProf.setTeleconsulta(false);
        consultaSemProf.setVideochamadaUrl(null);
        ConsultaResponseDTO dto = invokeToResponseDTO(consultaSemProf);
        assertEquals(21L, dto.getId());
        assertNull(dto.getProfissionalId());
        assertNull(dto.getProfissionalNome());
    }

    @Test
    void toResponseDTO_deveRetornarDTOComUsuarioDoProfissionalNulo() {
        Profissional profSemUsuario = new Profissional();
        profSemUsuario.setId(99L);
        profSemUsuario.setUsuario(null);
        Consulta consultaProfSemUsuario = new Consulta();
        consultaProfSemUsuario.setId(22L);
        consultaProfSemUsuario.setPaciente(paciente);
        consultaProfSemUsuario.setProfissional(profSemUsuario);
        consultaProfSemUsuario.setDataHora(LocalDateTime.now());
        consultaProfSemUsuario.setStatus("AGENDADA");
        consultaProfSemUsuario.setTeleconsulta(false);
        consultaProfSemUsuario.setVideochamadaUrl(null);
        ConsultaResponseDTO dto = invokeToResponseDTO(consultaProfSemUsuario);
        assertEquals(22L, dto.getId());
        assertEquals(99L, dto.getProfissionalId());
        assertNull(dto.getProfissionalNome());
    }

    @Test
    void criarParaProfissionalAutenticado_deveAgendarConsulta() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("profissional@email.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        profissional.getUsuario().setEmail("profissional@email.com");
        when(profissionalRepository.findAll()).thenReturn(List.of(profissional));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(pacienteRepository.findById(2L)).thenReturn(Optional.of(paciente));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);
        ConsultaResponseDTO dto = consultaService.criarParaProfissionalAutenticado(requestDTO);
        assertNotNull(dto);
        assertEquals(10L, dto.getId());
    }

    @Test
    void criarParaPacienteAutenticado_deveAgendarConsulta() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("paciente@email.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        paciente.getUsuario().setEmail("paciente@email.com");
        when(pacienteRepository.findByUsuarioEmail("paciente@email.com")).thenReturn(paciente);
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);
        ConsultaRequestDTO dtoReq = new ConsultaRequestDTO();
        dtoReq.setDataHora(consulta.getDataHora());
        dtoReq.setStatus("AGENDADA");
        dtoReq.setTeleconsulta(false);
        dtoReq.setVideochamadaUrl(null);
        ConsultaResponseDTO dto = consultaService.criarParaPacienteAutenticado(dtoReq);
        assertNotNull(dto);
        assertEquals(10L, dto.getId());
    }

    @Test
    void listarPorProfissionalAutenticado_deveRetornarConsultas() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("profissional@email.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        profissional.getUsuario().setEmail("profissional@email.com");
        when(profissionalRepository.findAll()).thenReturn(List.of(profissional));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(consultaRepository.findByProfissionalId(1L)).thenReturn(List.of(consulta));
        List<ConsultaResponseDTO> lista = consultaService.listarPorProfissionalAutenticado();
        assertEquals(1, lista.size());
        assertEquals(10L, lista.get(0).getId());
    }

    @Test
    void listarPorPacienteAutenticado_deveRetornarConsultas() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("paciente@email.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        paciente.getUsuario().setEmail("paciente@email.com");
        when(pacienteRepository.findByUsuarioEmail("paciente@email.com")).thenReturn(paciente);
        when(consultaRepository.findByPacienteId(2L)).thenReturn(List.of(consulta));
        List<ConsultaResponseDTO> lista = consultaService.listarPorPacienteAutenticado();
        assertEquals(1, lista.size());
        assertEquals(10L, lista.get(0).getId());
    }

    @Test
    void criarParaProfissionalAutenticado_deveLancarExcecaoSeProfissionalNaoEncontradoPorEmail() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("naoexiste@email.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(profissionalRepository.findAll()).thenReturn(List.of());
        assertThrows(IllegalArgumentException.class, () -> consultaService.criarParaProfissionalAutenticado(requestDTO));
    }

    @Test
    void criarParaProfissionalAutenticado_deveLancarExcecaoSeProfissionalNaoEncontradoPorId() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("profissional@email.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Profissional prof = new Profissional();
        Usuario usuario = new Usuario();
        usuario.setEmail("profissional@email.com");
        prof.setUsuario(usuario);
        prof.setId(99L);
        when(profissionalRepository.findAll()).thenReturn(List.of(prof));
        when(profissionalRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> consultaService.criarParaProfissionalAutenticado(requestDTO));
    }

    @Test
    void criarParaProfissionalAutenticado_deveLancarExcecaoSePacienteNaoEncontrado() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("profissional@email.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        profissional.getUsuario().setEmail("profissional@email.com");
        when(profissionalRepository.findAll()).thenReturn(List.of(profissional));
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(pacienteRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> consultaService.criarParaProfissionalAutenticado(requestDTO));
    }

    @Test
    void criarParaPacienteAutenticado_deveLancarExcecaoSePacienteNaoEncontrado() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("naoexiste@email.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(pacienteRepository.findByUsuarioEmail("naoexiste@email.com")).thenReturn(null);
        ConsultaRequestDTO dtoReq = new ConsultaRequestDTO();
        dtoReq.setDataHora(LocalDateTime.now());
        dtoReq.setStatus("AGENDADA");
        dtoReq.setTeleconsulta(false);
        dtoReq.setVideochamadaUrl(null);
        assertThrows(IllegalArgumentException.class, () -> consultaService.criarParaPacienteAutenticado(dtoReq));
    }

    @Test
    void listarPorProfissionalAutenticado_deveLancarExcecaoSeProfissionalNaoEncontradoPorEmail() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("naoexiste@email.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(profissionalRepository.findAll()).thenReturn(List.of());
        assertThrows(IllegalArgumentException.class, () -> consultaService.listarPorProfissionalAutenticado());
    }

    @Test
    void listarPorProfissionalAutenticado_deveLancarExcecaoSeProfissionalNaoEncontradoPorId() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("profissional@email.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Profissional prof = new Profissional();
        Usuario usuario = new Usuario();
        usuario.setEmail("profissional@email.com");
        prof.setUsuario(usuario);
        prof.setId(99L);
        when(profissionalRepository.findAll()).thenReturn(List.of(prof));
        when(profissionalRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> consultaService.listarPorProfissionalAutenticado());
    }

    @Test
    void listarPorPacienteAutenticado_deveLancarExcecaoSePacienteNaoEncontrado() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("naoexiste@email.com");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(pacienteRepository.findByUsuarioEmail("naoexiste@email.com")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> consultaService.listarPorPacienteAutenticado());
    }

    // Utilitário para acessar o método privado toResponseDTO
    private ConsultaResponseDTO invokeToResponseDTO(Consulta consulta) {
        try {
            java.lang.reflect.Method method = ConsultaService.class.getDeclaredMethod("toResponseDTO", Consulta.class);
            method.setAccessible(true);
            return (ConsultaResponseDTO) method.invoke(consultaService, consulta);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
} 