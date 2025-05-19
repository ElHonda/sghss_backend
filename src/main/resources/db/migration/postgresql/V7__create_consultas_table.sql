CREATE TABLE consultas (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(usuario_id),
    profissional_id BIGINT NOT NULL REFERENCES profissionais(usuario_id),
    data_hora TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL,
    teleconsulta BOOLEAN NOT NULL DEFAULT FALSE,
    videochamada_url VARCHAR(255),
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    CONSTRAINT fk_consulta_criado_por FOREIGN KEY (criado_por) REFERENCES usuarios(id),
    CONSTRAINT fk_consulta_atualizado_por FOREIGN KEY (atualizado_por) REFERENCES usuarios(id)
);