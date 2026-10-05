-- 02: regras adicionais. Execute depois de 01_create_tables.sql.
BEGIN;

ALTER TABLE usuario
    ADD CONSTRAINT uq_usuario_login UNIQUE (login),
    ADD CONSTRAINT ck_usuario_login CHECK (btrim(login) <> ''),
    ADD CONSTRAINT ck_usuario_senha CHECK (btrim(senha) <> '');

ALTER TABLE proprietario
    ADD CONSTRAINT uq_proprietario_cpf UNIQUE (cpf),
    ADD CONSTRAINT uq_proprietario_cnh UNIQUE (cnh),
    ADD CONSTRAINT ck_proprietario_nome CHECK (btrim(nome_completo) <> ''),
    ADD CONSTRAINT ck_proprietario_cpf CHECK (cpf ~ '^[0-9]{11}$'),
    ADD CONSTRAINT ck_proprietario_cnh CHECK (cnh IS NULL OR cnh ~ '^[0-9]{11}$');

ALTER TABLE veiculo
    ADD CONSTRAINT uq_veiculo_placa UNIQUE (placa),
    ADD CONSTRAINT uq_veiculo_renavam UNIQUE (renavam),
    ADD CONSTRAINT uq_veiculo_chassi UNIQUE (chassi),
    ADD CONSTRAINT ck_veiculo_status CHECK (status IN ('EM_ESTOQUE', 'RESERVADO', 'VENDIDO')),
    ADD CONSTRAINT ck_veiculo_quilometragem CHECK (quilometragem >= 0),
    ADD CONSTRAINT ck_veiculo_valor_compra CHECK (valor_compra >= 0),
    ADD CONSTRAINT ck_veiculo_valor_venda CHECK (valor_venda IS NULL OR valor_venda >= 0),
    ADD CONSTRAINT ck_veiculo_anos CHECK (ano_fabricacao >= 1886 AND ano_modelo >= ano_fabricacao),
    ADD CONSTRAINT ck_veiculo_placa CHECK (placa ~ '^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$'),
    ADD CONSTRAINT ck_veiculo_renavam CHECK (renavam ~ '^[0-9]{11}$'),
    ADD CONSTRAINT ck_veiculo_chassi CHECK (chassi ~ '^[A-HJ-NPR-Z0-9]{17}$'),
    ADD CONSTRAINT ck_veiculo_marca CHECK (btrim(marca) <> ''),
    ADD CONSTRAINT ck_veiculo_modelo CHECK (btrim(modelo) <> ''),
    ADD CONSTRAINT ck_veiculo_cor CHECK (btrim(cor) <> ''),
    ADD CONSTRAINT ck_veiculo_combustivel CHECK (btrim(tipo_combustivel) <> '');

ALTER TABLE documento
    ADD CONSTRAINT ck_documento_tipo CHECK (tipo_documento IN (
        'ATPV', 'CRLV', 'CRV', 'LAUDO_CAUTELAR', 'CONTRATO_COMPRA',
        'CONTRATO_VENDA', 'NOTA_FISCAL', 'OUTRO'
    )),
    ADD CONSTRAINT ck_documento_nome CHECK (btrim(nome) <> ''),
    ADD CONSTRAINT ck_documento_caminho CHECK (btrim(caminho_arquivo) <> '');

CREATE INDEX idx_veiculo_proprietario ON veiculo (id_proprietario);
CREATE INDEX idx_documento_veiculo ON documento (id_veiculo);

COMMIT;