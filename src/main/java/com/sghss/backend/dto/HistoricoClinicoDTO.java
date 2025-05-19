package com.sghss.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoClinicoDTO {
    private Long id;
    private String descricao;
    private LocalDateTime data;
    private Long pacienteId;
    private String pacienteNome;
} 