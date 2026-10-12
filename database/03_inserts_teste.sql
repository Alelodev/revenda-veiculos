-- 03: dados fictícios para estudo.
--
-- Antes de executar este script, crie pelo menos um usuário
-- através da aplicação.
--
-- O script utilizará o primeiro usuário encontrado.
--
-- Execute uma vez após os scripts 01 e 02.

BEGIN;

DO $$
BEGIN

    IF NOT EXISTS (
        SELECT 1
        FROM usuario
    ) THEN
        RAISE EXCEPTION
            'Nenhum usuário cadastrado. Crie um usuário pela aplicação antes de executar este script.';
END IF;

END $$;


WITH usuario_teste AS (

    SELECT id_usuario
    FROM usuario
    ORDER BY id_usuario
    LIMIT 1

    ),

    novo_proprietario AS (

INSERT INTO proprietario (
    nome_completo,
    cpf,
    telefone,
    endereco,
    email,
    cnh
)

VALUES (
    'João da Silva',
    '12345678909',
    '11999990000',
    'Rua Exemplo, 100 - São Paulo/SP',
    'joao.silva@example.com',
    '12345678901'
    )

    RETURNING id_proprietario

    ),

    novo_veiculo AS (

INSERT INTO veiculo (
    id_proprietario,
    id_usuario,
    marca,
    modelo,
    ano_fabricacao,
    ano_modelo,
    placa,
    cor,
    tipo_combustivel,
    renavam,
    chassi,
    quilometragem,
    valor_compra,
    valor_venda,
    status
)

SELECT
    p.id_proprietario,
    u.id_usuario,
    'Toyota',
    'Corolla XEi',
    2020,
    2021,
    'ABC1D23',
    'Prata',
    'Flex',
    '12345678901',
    '9BRBD48E0M1234567',
    45000,
    95000.00,
    109900.00,
    'EM_ESTOQUE'

FROM novo_proprietario p
    CROSS JOIN usuario_teste u

    RETURNING id_veiculo
    )

INSERT INTO documento (
    id_veiculo,
    nome,
    tipo_documento,
    caminho_arquivo,
    data_upload
)

SELECT
    id_veiculo,
    'CRLV 2026',
    'CRLV',
    'uploads/veiculos/' ||
    id_veiculo ||
    '/crlv-2026.pdf',
    TIMESTAMPTZ '2026-10-05 10:00:00-03'

FROM novo_veiculo;

COMMIT;