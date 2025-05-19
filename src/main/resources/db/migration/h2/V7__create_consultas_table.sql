CREATE TABLE consultas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    profissional_id BIGINT NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL,
    teleconsulta BOOLEAN NOT NULL DEFAULT FALSE,
    videochamada_url VARCHAR(255),
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    atualizado_em TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    FOREIGN KEY (paciente_id) REFERENCES pacientes(usuario_id),
    FOREIGN KEY (profissional_id) REFERENCES profissionais(usuario_id),
    FOREIGN KEY (criado_por) REFERENCES usuarios(id),
    FOREIGN KEY (atualizado_por) REFERENCES usuarios(id)
);