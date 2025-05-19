package com.sghss.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConsultaRequestDTO {
    private Long pacienteId;
    private LocalDateTime dataHora;
    private boolean teleconsulta;
    private String videochamadaUrl;
    private String status;
} 