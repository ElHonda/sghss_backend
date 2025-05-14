-- Criação da tabela profissionais
CREATE TABLE profissionais (
    usuario_id BIGINT PRIMARY KEY,
    tipo_profissional_id BIGINT NOT NULL,
    registro_profissional VARCHAR(255),
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT,
    CONSTRAINT fk_profissional_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_profissional_tipo FOREIGN KEY (tipo_profissional_id) REFERENCES tipos_profissional(id),
    CONSTRAINT fk_prof_criado_por FOREIGN KEY (criado_por) REFERENCES usuarios(id),
    CONSTRAINT fk_prof_atualizado_por FOREIGN KEY (atualizado_por) REFERENCES usuarios(id)
);

-- Remover colunas específicas de profissional da tabela usuarios
ALTER TABLE usuarios DROP COLUMN IF EXISTS tipo_profissional_id;
ALTER TABLE usuarios DROP COLUMN IF EXISTS registro_profissional; 