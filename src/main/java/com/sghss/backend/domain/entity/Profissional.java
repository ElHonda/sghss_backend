package com.sghss.backend.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "profissionais")
@Data
@EqualsAndHashCode(callSuper = true)
public class Profissional extends Auditable {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "tipo_profissional_id")
    private TipoProfissional tipoProfissional;

    @Column(name = "registro_profissional")
    private String registroProfissional;
} 