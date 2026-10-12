-- Consultas de leitura.
--
-- Execute depois dos scripts 01 e 02.
-- Algumas consultas terão resultados apenas após existirem dados.


-- =====================================================
-- USUARIOS
-- =====================================================

-- Não exibir hash da senha.

SELECT
    id_usuario,
    login,
    perfil
FROM usuario
ORDER BY id_usuario;


-- =====================================================
-- PROPRIETARIOS
-- =====================================================

SELECT *
FROM proprietario
ORDER BY id_proprietario;


-- =====================================================
-- VEICULOS
-- =====================================================

SELECT *
FROM veiculo
ORDER BY id_veiculo;


-- =====================================================
-- DOCUMENTOS
-- =====================================================

SELECT *
FROM documento
ORDER BY id_documento;


-- =====================================================
-- VEICULOS DISPONIVEIS
-- =====================================================

SELECT
    placa,
    marca,
    modelo,
    ano_modelo,
    quilometragem,
    valor_venda
FROM veiculo
WHERE status = 'EM_ESTOQUE'
ORDER BY marca, modelo;


-- =====================================================
-- VEICULOS POR USUARIO
-- =====================================================

SELECT
    u.login AS usuario,
    v.id_veiculo,
    v.placa,
    v.marca,
    v.modelo,
    v.status
FROM usuario u
         JOIN veiculo v
              ON v.id_usuario = u.id_usuario
ORDER BY u.login, v.id_veiculo;


-- =====================================================
-- PROPRIETARIO + USUARIO + VEICULO
-- =====================================================

SELECT
    u.login AS cadastrado_por,
    p.nome_completo AS proprietario,
    v.placa,
    v.marca,
    v.modelo,
    v.status
FROM veiculo v
         JOIN usuario u
              ON u.id_usuario = v.id_usuario
         JOIN proprietario p
              ON p.id_proprietario = v.id_proprietario
ORDER BY u.login, p.nome_completo;


-- =====================================================
-- PROPRIETARIO + VEICULO + DOCUMENTO
-- =====================================================

SELECT
    u.login AS cadastrado_por,
    p.nome_completo AS proprietario,
    v.placa,
    v.marca,
    v.modelo,
    v.status,
    d.nome AS documento,
    d.tipo_documento,
    d.caminho_arquivo,
    d.data_upload

FROM proprietario p

         JOIN veiculo v
              ON v.id_proprietario = p.id_proprietario

         JOIN usuario u
              ON u.id_usuario = v.id_usuario

         JOIN documento d
              ON d.id_veiculo = v.id_veiculo

ORDER BY
    u.login,
    p.nome_completo,
    v.placa,
    d.data_upload;


-- =====================================================
-- VEICULOS SEM OU COM DOCUMENTOS
-- =====================================================

SELECT
    u.login AS cadastrado_por,
    p.nome_completo AS proprietario,
    v.placa,
    v.modelo,
    d.nome AS documento,
    d.tipo_documento

FROM proprietario p

         JOIN veiculo v
              ON v.id_proprietario = p.id_proprietario

         JOIN usuario u
              ON u.id_usuario = v.id_usuario

         LEFT JOIN documento d
                   ON d.id_veiculo = v.id_veiculo

ORDER BY
    v.placa,
    d.nome;


-- =====================================================
-- QUANTIDADE DE DOCUMENTOS
-- =====================================================

SELECT
    v.id_veiculo,
    v.placa,
    v.modelo,
    u.login AS cadastrado_por,
    COUNT(d.id_documento) AS total_documentos

FROM veiculo v

         JOIN usuario u
              ON u.id_usuario = v.id_usuario

         LEFT JOIN documento d
                   ON d.id_veiculo = v.id_veiculo

GROUP BY
    v.id_veiculo,
    v.placa,
    v.modelo,
    u.login

ORDER BY v.placa;


-- =====================================================
-- QUANTIDADE DE VEICULOS POR USUARIO
-- =====================================================

SELECT
    u.id_usuario,
    u.login,
    u.perfil,
    COUNT(v.id_veiculo) AS total_veiculos

FROM usuario u

         LEFT JOIN veiculo v
                   ON v.id_usuario = u.id_usuario

GROUP BY
    u.id_usuario,
    u.login,
    u.perfil

ORDER BY u.login;