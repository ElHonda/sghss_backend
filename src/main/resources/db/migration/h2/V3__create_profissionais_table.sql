-- Criação da tabela profissionais
CREATE TABLE profissionais (
    usuario_id BIGINT PRIMARY KEY,
    tipo_profissional_id BIGINT NOT NULL,
    registro_profissional VARCHAR(255),
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP(),
    atualizado_em TIMESTAMP,
    criado_por BIGINT,
    atualizado_por BIGINT
);

-- Remover colunas específicas de profissional da tabela usuarios
ALTER TABLE usuarios DROP COLUMN IF EXISTS tipo_profissional_id;
ALTER TABLE usuarios DROP COLUMN IF EXISTS registro_profissional; 