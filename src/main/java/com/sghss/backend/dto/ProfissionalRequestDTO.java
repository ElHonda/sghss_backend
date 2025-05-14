package com.sghss.backend.dto;

import lombok.Data;

@Data
public class ProfissionalRequestDTO {
    private String nome;
    private String email;
    private String senha;
    private Long tipoProfissionalId;
    private String registroProfissional;
} 