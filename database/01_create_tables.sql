-- 01: criação das tabelas.
-- Execute em um banco vazio.

BEGIN;

CREATE TABLE usuario (
                         id_usuario BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                         login VARCHAR(100) NOT NULL,
                         senha VARCHAR(255) NOT NULL,
                         perfil VARCHAR(20) NOT NULL DEFAULT 'USER'
);

CREATE TABLE proprietario (
                              id_proprietario BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              nome_completo VARCHAR(150) NOT NULL,
                              cpf VARCHAR(11) NOT NULL,
                              telefone VARCHAR(20),
                              endereco TEXT,
                              email VARCHAR(254),
                              cnh VARCHAR(11)
);

CREATE TABLE veiculo (
                         id_veiculo BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

                         id_proprietario BIGINT NOT NULL,
                         id_usuario BIGINT NOT NULL,

                         marca VARCHAR(60) NOT NULL,
                         modelo VARCHAR(100) NOT NULL,

                         ano_fabricacao SMALLINT,
                         ano_modelo SMALLINT,

                         placa VARCHAR(8) NOT NULL,

                         cor VARCHAR(40),
                         tipo_combustivel VARCHAR(40),

                         renavam VARCHAR(11),
                         chassi VARCHAR(17),

                         quilometragem INTEGER DEFAULT 0,

                         valor_compra NUMERIC(12,2),
                         valor_venda NUMERIC(12,2),

                         status VARCHAR(20) DEFAULT 'EM_ESTOQUE',

                         CONSTRAINT fk_veiculo_proprietario
                             FOREIGN KEY (id_proprietario)
                                 REFERENCES proprietario (id_proprietario)
                                 ON DELETE RESTRICT,

                         CONSTRAINT fk_veiculo_usuario
                             FOREIGN KEY (id_usuario)
                                 REFERENCES usuario (id_usuario)
                                 ON DELETE RESTRICT
);

CREATE TABLE documento (
                           id_documento BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

                           id_veiculo BIGINT NOT NULL,

                           nome VARCHAR(150) NOT NULL,
                           tipo_documento VARCHAR(30) NOT NULL,
                           caminho_arquivo TEXT NOT NULL,

                           data_upload TIMESTAMP WITH TIME ZONE
                               NOT NULL
                               DEFAULT CURRENT_TIMESTAMP,

                           CONSTRAINT fk_documento_veiculo
                               FOREIGN KEY (id_veiculo)
                                   REFERENCES veiculo (id_veiculo)
                                   ON DELETE RESTRICT
);

COMMIT;