-- Adiciona colunas de auditoria na tabela usuarios
ALTER TABLE usuarios ADD COLUMN criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP();
ALTER TABLE usuarios ADD COLUMN atualizado_em TIMESTAMP;
ALTER TABLE usuarios ADD COLUMN criado_por BIGINT;
ALTER TABLE usuarios ADD COLUMN atualizado_por BIGINT;

-- Adiciona colunas de auditoria na tabela tipos_profissional
ALTER TABLE tipos_profissional ADD COLUMN criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP();
ALTER TABLE tipos_profissional ADD COLUMN atualizado_em TIMESTAMP;
ALTER TABLE tipos_profissional ADD COLUMN criado_por BIGINT;
ALTER TABLE tipos_profissional ADD COLUMN atualizado_por BIGINT; 