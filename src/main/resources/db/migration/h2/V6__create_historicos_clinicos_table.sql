CREATE TABLE historicos_clinicos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paciente_id BIGINT NOT NULL,
    descricao TEXT NOT NULL,
    data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    atualizado_em TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id),
    FOREIGN KEY (criado_por) REFERENCES usuarios(id),
    FOREIGN KEY (atualizado_por) REFERENCES usuarios(id)
); 