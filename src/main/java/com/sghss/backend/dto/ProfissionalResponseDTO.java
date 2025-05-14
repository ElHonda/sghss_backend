package com.sghss.backend.dto;

import lombok.Data;

@Data
public class ProfissionalResponseDTO {
    private Long id;
    private Long usuarioId;
    private String nomeUsuario;
    private String emailUsuario;
    private Long tipoProfissionalId;
    private String tipoProfissionalNome;
    private String registroProfissional;
} 