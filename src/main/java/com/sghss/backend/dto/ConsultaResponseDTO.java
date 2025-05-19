package com.sghss.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConsultaResponseDTO {
    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private Long profissionalId;
    private String profissionalNome;
    private LocalDateTime dataHora;
    private String status;
    private boolean teleconsulta;
    private String videochamadaUrl;
} 