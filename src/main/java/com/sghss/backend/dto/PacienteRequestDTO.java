package com.sghss.backend.dto;

import lombok.Data;

@Data
public class PacienteRequestDTO {
    private String nome;
    private String dataNascimento;
    private String cpf;
    private String telefone;
    private String endereco;
    private String email;
    private String senha;
} 