-- 03: dados fictícios para estudo. Execute uma vez após os scripts 01 e 02.
-- IDs são gerados pelo PostgreSQL; os relacionamentos usam RETURNING.
-- Nenhum usuário é inserido: senha deverá receber um hash gerado pela aplicação.
BEGIN;

WITH novo_proprietario AS (
    INSERT INTO proprietario (nome_completo, cpf, telefone, endereco, email, cnh)
    VALUES ('João da Silva', '12345678909', '11999990000',
            'Rua Exemplo, 100 - São Paulo/SP', 'joao.silva@example.com', '12345678901')
    RETURNING id_proprietario
), novo_veiculo AS (
    INSERT INTO veiculo (
        id_proprietario, marca, modelo, ano_fabricacao, ano_modelo, placa,
        cor, tipo_combustivel, renavam, chassi, quilometragem,
        valor_compra, valor_venda, status
    )
    SELECT id_proprietario, 'Toyota', 'Corolla XEi', 2020, 2021, 'ABC1D23',
           'Prata', 'FLEX', '12345678901', '9BRBD48E0M1234567', 45000,
           95000.00, 109900.00, 'EM_ESTOQUE'
    FROM novo_proprietario
    RETURNING id_veiculo
)
INSERT INTO documento (id_veiculo, nome, tipo_documento, caminho_arquivo, data_upload)
SELECT id_veiculo, 'CRLV 2026', 'CRLV',
       'uploads/documentos/ABC1D23/crlv-2026.pdf',
       TIMESTAMPTZ '2026-10-05 10:00:00-03'
FROM novo_veiculo;

COMMIT;