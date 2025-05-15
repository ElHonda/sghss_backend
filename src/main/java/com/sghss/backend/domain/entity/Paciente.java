package com.sghss.backend.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "pacientes")
@Data
@EqualsAndHashCode(callSuper = true)
public class Paciente extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String dataNascimento;

    @Column(nullable = false)
    private String cpf; // criptografado (usar utilitário de criptografia)

    @Column(nullable = false)
    private String telefone; // criptografado (usar utilitário de criptografia)

    @Column(nullable = false)
    private String endereco; // criptografado (usar utilitário de criptografia)
} 