-- 02: regras adicionais.
-- Execute depois de 01_create_tables.sql.

BEGIN;

-- =====================================================
-- USUARIO
-- =====================================================

ALTER TABLE usuario
    ADD CONSTRAINT uq_usuario_login
        UNIQUE (login),

    ADD CONSTRAINT ck_usuario_login
        CHECK (btrim(login) <> ''),

    ADD CONSTRAINT ck_usuario_senha
        CHECK (btrim(senha) <> ''),

    ADD CONSTRAINT ck_usuario_perfil
        CHECK (perfil IN ('USER', 'ADMIN'));


-- =====================================================
-- PROPRIETARIO
-- =====================================================

ALTER TABLE proprietario
    ADD CONSTRAINT uq_proprietario_cpf
        UNIQUE (cpf),

    ADD CONSTRAINT uq_proprietario_cnh
        UNIQUE (cnh),

    ADD CONSTRAINT ck_proprietario_nome
        CHECK (btrim(nome_completo) <> ''),

    ADD CONSTRAINT ck_proprietario_cpf
        CHECK (cpf ~ '^[0-9]{11}$'),

    ADD CONSTRAINT ck_proprietario_cnh
        CHECK (
            cnh IS NULL
            OR cnh ~ '^[0-9]{11}$'
        );


-- =====================================================
-- VEICULO
-- =====================================================

ALTER TABLE veiculo
    ADD CONSTRAINT uq_veiculo_placa
        UNIQUE (placa),

    ADD CONSTRAINT uq_veiculo_renavam
        UNIQUE (renavam),

    ADD CONSTRAINT uq_veiculo_chassi
        UNIQUE (chassi),

    ADD CONSTRAINT ck_veiculo_status
        CHECK (
            status IS NULL
            OR status IN (
                'EM_ESTOQUE',
                'RESERVADO',
                'VENDIDO'
            )
        ),

    ADD CONSTRAINT ck_veiculo_quilometragem
        CHECK (
            quilometragem IS NULL
            OR quilometragem >= 0
        ),

    ADD CONSTRAINT ck_veiculo_valor_compra
        CHECK (
            valor_compra IS NULL
            OR valor_compra >= 0
        ),

    ADD CONSTRAINT ck_veiculo_valor_venda
        CHECK (
            valor_venda IS NULL
            OR valor_venda >= 0
        ),

    ADD CONSTRAINT ck_veiculo_anos
        CHECK (
            ano_fabricacao IS NULL
            OR ano_modelo IS NULL
            OR (
                ano_fabricacao >= 1886
                AND ano_modelo >= ano_fabricacao
            )
        ),

    ADD CONSTRAINT ck_veiculo_placa
        CHECK (
            placa ~ '^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$'
        ),

    ADD CONSTRAINT ck_veiculo_renavam
        CHECK (
            renavam IS NULL
            OR renavam ~ '^[0-9]{11}$'
        ),

    ADD CONSTRAINT ck_veiculo_chassi
        CHECK (
            chassi IS NULL
            OR chassi ~ '^[A-HJ-NPR-Z0-9]{17}$'
        ),

    ADD CONSTRAINT ck_veiculo_marca
        CHECK (btrim(marca) <> ''),

    ADD CONSTRAINT ck_veiculo_modelo
        CHECK (btrim(modelo) <> ''),

    ADD CONSTRAINT ck_veiculo_cor
        CHECK (
            cor IS NULL
            OR btrim(cor) <> ''
        ),

    ADD CONSTRAINT ck_veiculo_combustivel
        CHECK (
            tipo_combustivel IS NULL
            OR btrim(tipo_combustivel) <> ''
        );


-- =====================================================
-- DOCUMENTO
-- =====================================================

ALTER TABLE documento
    ADD CONSTRAINT ck_documento_tipo
        CHECK (
            tipo_documento IN (
                               'ATPV',
                               'CRLV',
                               'CRV',
                               'LAUDO_CAUTELAR',
                               'CONTRATO_COMPRA',
                               'CONTRATO_VENDA',
                               'NOTA_FISCAL',
                               'OUTRO'
                )
            ),

    ADD CONSTRAINT ck_documento_nome
        CHECK (btrim(nome) <> ''),

    ADD CONSTRAINT ck_documento_caminho
        CHECK (btrim(caminho_arquivo) <> '');


-- =====================================================
-- INDICES
-- =====================================================

CREATE INDEX idx_veiculo_proprietario
    ON veiculo (id_proprietario);

CREATE INDEX idx_veiculo_usuario
    ON veiculo (id_usuario);

CREATE INDEX idx_documento_veiculo
    ON documento (id_veiculo);

COMMIT;