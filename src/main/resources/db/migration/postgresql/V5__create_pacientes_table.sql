CREATE TABLE pacientes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    nome VARCHAR(255) NOT NULL,
    data_nascimento VARCHAR(20) NOT NULL,
    cpf VARCHAR(255) NOT NULL,
    telefone VARCHAR(255) NOT NULL,
    endereco VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    CONSTRAINT fk_paciente_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_paciente_criado_por FOREIGN KEY (criado_por) REFERENCES usuarios(id),
    CONSTRAINT fk_paciente_atualizado_por FOREIGN KEY (atualizado_por) REFERENCES usuarios(id)
); 