-- Consultas de leitura. Execute depois dos scripts 01, 02 e, opcionalmente, 03.
-- Não exibir hashes de senha em consultas de listagem.
SELECT id_usuario, login FROM usuario ORDER BY id_usuario;
SELECT * FROM proprietario ORDER BY id_proprietario;
SELECT * FROM veiculo ORDER BY id_veiculo;
SELECT * FROM documento ORDER BY id_documento;

-- Veículos disponíveis.
SELECT placa, marca, modelo, ano_modelo, quilometragem, valor_venda
FROM veiculo
WHERE status = 'EM_ESTOQUE'
ORDER BY marca, modelo;

-- Proprietário, veículo e documentos cadastrados (apenas veículos com documentos).
SELECT p.nome_completo AS proprietario, v.placa, v.marca, v.modelo,
       v.status, d.nome AS documento, d.tipo_documento,
       d.caminho_arquivo, d.data_upload
FROM proprietario AS p
JOIN veiculo AS v ON v.id_proprietario = p.id_proprietario
JOIN documento AS d ON d.id_veiculo = v.id_veiculo
ORDER BY p.nome_completo, v.placa, d.data_upload;

-- Inclui veículos ainda sem documentos. Cada documento gera uma linha.
SELECT p.nome_completo AS proprietario, v.placa, v.modelo,
       d.nome AS documento, d.tipo_documento
FROM proprietario AS p
JOIN veiculo AS v ON v.id_proprietario = p.id_proprietario
LEFT JOIN documento AS d ON d.id_veiculo = v.id_veiculo
ORDER BY v.placa, d.nome;

-- Quantidade de documentos por veículo, incluindo zero.
SELECT v.id_veiculo, v.placa, v.modelo, COUNT(d.id_documento) AS total_documentos
FROM veiculo AS v
LEFT JOIN documento AS d ON d.id_veiculo = v.id_veiculo
GROUP BY v.id_veiculo, v.placa, v.modelo
ORDER BY v.placa;