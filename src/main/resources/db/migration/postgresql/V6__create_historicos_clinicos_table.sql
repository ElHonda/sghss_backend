CREATE TABLE historicos_clinicos (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
    descricao TEXT NOT NULL,
    data TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    CONSTRAINT fk_historico_criado_por FOREIGN KEY (criado_por) REFERENCES usuarios(id),
    CONSTRAINT fk_historico_atualizado_por FOREIGN KEY (atualizado_por) REFERENCES usuarios(id)
); 