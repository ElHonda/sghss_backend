package com.sghss.backend.service;

import com.sghss.backend.domain.entity.Profissional;
import com.sghss.backend.domain.entity.TipoProfissional;
import com.sghss.backend.domain.entity.Usuario;
import com.sghss.backend.domain.enums.Role;
import com.sghss.backend.dto.ProfissionalRequestDTO;
import com.sghss.backend.dto.ProfissionalResponseDTO;
import com.sghss.backend.repository.ProfissionalRepository;
import com.sghss.backend.repository.TipoProfissionalRepository;
import com.sghss.backend.repository.UsuarioRepository;
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
class ProfissionalServiceTest {

    @Mock
    private ProfissionalRepository profissionalRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private TipoProfissionalRepository tipoProfissionalRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProfissionalService service;

    private ProfissionalRequestDTO requestDTO;
    private Profissional profissional;
    private Usuario usuario;
    private TipoProfissional tipoProfissional;

    @BeforeEach
    void setUp() {
        requestDTO = new ProfissionalRequestDTO();
        requestDTO.setNome("Profissional Teste");
        requestDTO.setEmail("profissional@teste.com");
        requestDTO.setSenha("senha123");
        requestDTO.setTipoProfissionalId(1L);
        requestDTO.setRegistroProfissional("12345");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Profissional Teste");
        usuario.setEmail("profissional@teste.com");
        usuario.setSenha("senha123");
        usuario.setRole(Role.PROFISSIONAL);

        tipoProfissional = new TipoProfissional();
        tipoProfissional.setId(1L);
        tipoProfissional.setNome("Fisioterapeuta");

        profissional = new Profissional();
        profissional.setId(1L);
        profissional.setUsuario(usuario);
        profissional.setTipoProfissional(tipoProfissional);
        profissional.setRegistroProfissional("12345");
    }

    @Test
    void deveCriarProfissional() {
        when(passwordEncoder.encode(any())).thenReturn("senha123");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(tipoProfissionalRepository.findById(1L)).thenReturn(Optional.of(tipoProfissional));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);
        ProfissionalResponseDTO response = service.criar(requestDTO);
        assertNotNull(response);
        assertEquals("Profissional Teste", response.getNomeUsuario());
    }

    @Test
    void deveListarTodosProfissionais() {
        when(profissionalRepository.findAll()).thenReturn(Arrays.asList(profissional));
        List<ProfissionalResponseDTO> lista = service.listarTodos();
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals("Profissional Teste", lista.get(0).getNomeUsuario());
    }

    @Test
    void deveBuscarPorIdExistente() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        Optional<ProfissionalResponseDTO> response = service.buscarPorId(1L);
        assertTrue(response.isPresent());
        assertEquals("Profissional Teste", response.get().getNomeUsuario());
    }

    @Test
    void deveRetornarVazioAoBuscarPorIdInexistente() {
        when(profissionalRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<ProfissionalResponseDTO> response = service.buscarPorId(2L);
        assertTrue(response.isEmpty());
    }

    @Test
    void deveAtualizarProfissionalExistente() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(tipoProfissionalRepository.findById(1L)).thenReturn(Optional.of(tipoProfissional));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);
        requestDTO.setTipoProfissionalId(1L);
        requestDTO.setRegistroProfissional("54321");
        Optional<ProfissionalResponseDTO> response = service.atualizar(1L, requestDTO);
        assertTrue(response.isPresent());
        assertEquals("54321", response.get().getRegistroProfissional());
    }

    @Test
    void deveRetornarVazioAoAtualizarProfissionalInexistente() {
        when(profissionalRepository.findById(anyLong())).thenReturn(Optional.empty());
        Optional<ProfissionalResponseDTO> response = service.atualizar(2L, requestDTO);
        assertTrue(response.isEmpty());
    }

    @Test
    void deveDeletarProfissionalExistente() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        doNothing().when(profissionalRepository).delete(profissional);
        boolean deleted = service.deletar(1L);
        assertTrue(deleted);
        verify(profissionalRepository).delete(profissional);
    }

    @Test
    void naoDeveDeletarProfissionalInexistente() {
        when(profissionalRepository.findById(anyLong())).thenReturn(Optional.empty());
        boolean deleted = service.deletar(2L);
        assertFalse(deleted);
    }

    @Test
    void deveConverterParaResponseDTOComCamposCompletos() throws Exception {
        Profissional profissionalCompleto = new Profissional();
        profissionalCompleto.setId(10L);
        Usuario usuario = new Usuario();
        usuario.setId(20L);
        usuario.setNome("Nome Teste");
        usuario.setEmail("email@teste.com");
        profissionalCompleto.setUsuario(usuario);
        TipoProfissional tipo = new TipoProfissional();
        tipo.setId(30L);
        tipo.setNome("Tipo Teste");
        profissionalCompleto.setTipoProfissional(tipo);
        profissionalCompleto.setRegistroProfissional("REG123");

        var method = ProfissionalService.class.getDeclaredMethod("toResponseDTO", Profissional.class);
        method.setAccessible(true);
        ProfissionalResponseDTO dto = (ProfissionalResponseDTO) method.invoke(service, profissionalCompleto);
        assertEquals(10L, dto.getId());
        assertEquals(20L, dto.getUsuarioId());
        assertEquals("Nome Teste", dto.getNomeUsuario());
        assertEquals("email@teste.com", dto.getEmailUsuario());
        assertEquals(30L, dto.getTipoProfissionalId());
        assertEquals("Tipo Teste", dto.getTipoProfissionalNome());
        assertEquals("REG123", dto.getRegistroProfissional());
    }

    @Test
    void deveAtualizarApenasRegistroProfissional() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);
        ProfissionalRequestDTO dto = new ProfissionalRequestDTO();
        dto.setRegistroProfissional("NOVO_REG");
        Optional<ProfissionalResponseDTO> response = service.atualizar(1L, dto);
        assertTrue(response.isPresent());
        assertEquals("NOVO_REG", response.get().getRegistroProfissional());
    }

    @Test
    void deveConverterParaResponseDTOSemTipoProfissional() throws Exception {
        Profissional profissionalSemTipo = new Profissional();
        profissionalSemTipo.setId(11L);
        Usuario usuario = new Usuario();
        usuario.setId(21L);
        usuario.setNome("Sem Tipo");
        usuario.setEmail("sem@tipo.com");
        profissionalSemTipo.setUsuario(usuario);
        profissionalSemTipo.setTipoProfissional(null);
        profissionalSemTipo.setRegistroProfissional("REGSEM");

        var method = ProfissionalService.class.getDeclaredMethod("toResponseDTO", Profissional.class);
        method.setAccessible(true);
        ProfissionalResponseDTO dto = (ProfissionalResponseDTO) method.invoke(service, profissionalSemTipo);
        assertEquals(11L, dto.getId());
        assertEquals(21L, dto.getUsuarioId());
        assertEquals("Sem Tipo", dto.getNomeUsuario());
        assertEquals("sem@tipo.com", dto.getEmailUsuario());
        assertNull(dto.getTipoProfissionalId());
        assertNull(dto.getTipoProfissionalNome());
        assertEquals("REGSEM", dto.getRegistroProfissional());
    }

    @Test
    void deveAtualizarSemAlterarCamposQuandoDTOVazio() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(profissionalRepository.save(any(Profissional.class))).thenReturn(profissional);
        ProfissionalRequestDTO dto = new ProfissionalRequestDTO(); // todos campos nulos
        Optional<ProfissionalResponseDTO> response = service.atualizar(1L, dto);
        assertTrue(response.isPresent());
        // Os campos permanecem os mesmos
        assertEquals("12345", response.get().getRegistroProfissional());
        assertEquals(1L, response.get().getTipoProfissionalId());
    }

    @Test
    void deveLancarExcecaoAoCriarComTipoProfissionalInexistente() {
        when(passwordEncoder.encode(any())).thenReturn("senha123");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(tipoProfissionalRepository.findById(99L)).thenReturn(Optional.empty());
        ProfissionalRequestDTO dto = new ProfissionalRequestDTO();
        dto.setNome("Profissional Teste");
        dto.setEmail("profissional@teste.com");
        dto.setSenha("senha123");
        dto.setTipoProfissionalId(99L);
        dto.setRegistroProfissional("12345");
        assertThrows(IllegalArgumentException.class, () -> service.criar(dto));
    }

    @Test
    void deveLancarExcecaoAoAtualizarComTipoProfissionalInexistente() {
        when(profissionalRepository.findById(1L)).thenReturn(Optional.of(profissional));
        when(tipoProfissionalRepository.findById(99L)).thenReturn(Optional.empty());
        ProfissionalRequestDTO dto = new ProfissionalRequestDTO();
        dto.setTipoProfissionalId(99L);
        assertThrows(IllegalArgumentException.class, () -> service.atualizar(1L, dto));
    }
} 