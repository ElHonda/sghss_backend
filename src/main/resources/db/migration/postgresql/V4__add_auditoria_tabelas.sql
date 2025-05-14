-- Adiciona colunas de auditoria na tabela usuarios
ALTER TABLE usuarios
    ADD COLUMN criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    ADD COLUMN atualizado_em TIMESTAMP,
    ADD COLUMN criado_por BIGINT,
    ADD COLUMN atualizado_por BIGINT,
    ADD CONSTRAINT fk_usuario_criado_por FOREIGN KEY (criado_por) REFERENCES usuarios(id),
    ADD CONSTRAINT fk_usuario_atualizado_por FOREIGN KEY (atualizado_por) REFERENCES usuarios(id);

-- Adiciona colunas de auditoria na tabela tipos_profissional
ALTER TABLE tipos_profissional
    ADD COLUMN criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    ADD COLUMN atualizado_em TIMESTAMP,
    ADD COLUMN criado_por BIGINT,
    ADD COLUMN atualizado_por BIGINT,
    ADD CONSTRAINT fk_tipo_prof_criado_por FOREIGN KEY (criado_por) REFERENCES usuarios(id),
    ADD CONSTRAINT fk_tipo_prof_atualizado_por FOREIGN KEY (atualizado_por) REFERENCES usuarios(id); 