# Revenda de Veículos

Projeto em desenvolvimento para cadastrar proprietários e veículos de uma revenda, acompanhar a situação do estoque e organizar documentos associados a cada veículo.

Aqui estou subindo estrutura inicial do banco PostgreSQL e consultas de estudo. A aplicação Java ainda será implementada.

## Stack planejada

- Java: linguagem da aplicação.
- Spring Boot: configuração e desenvolvimento da aplicação web.
- Spring Data JPA / Hibernate: persistência e mapeamento das entidades.
- PostgreSQL: banco de dados relacional.
- Thymeleaf: páginas e formulários da interface web.

## Estrutura atual

```text
revenda-veiculos/
├── README.md
└── database/
    ├── 01_create_tables.sql
    ├── 02_constraints.sql
    ├── 03_inserts_teste.sql
    └── consultas.sql
```

A pasta `src` e os arquivos de configuração serão criados ao iniciar o projeto Spring Boot. Os PDFs não estão incluídos; o banco guarda somente o caminho do arquivo.

## Modelo relacional

```text
usuario (
  id_usuario PK, login UNIQUE, senha
)

proprietario (
  id_proprietario PK, nome_completo, cpf UNIQUE,
  telefone, endereco, email, cnh UNIQUE
)

veiculo (
  id_veiculo PK, id_proprietario FK -> proprietario.id_proprietario,
  marca, modelo, ano_fabricacao, ano_modelo, placa UNIQUE, cor,
  tipo_combustivel, renavam UNIQUE, chassi UNIQUE, quilometragem,
  valor_compra, valor_venda, status
)

documento (
  id_documento PK, id_veiculo FK -> veiculo.id_veiculo,
  nome, tipo_documento, caminho_arquivo, data_upload
)

proprietario (1) ─── (N) veiculo (1) ─── (N) documento
usuario: tabela isolada, sem relacionamento neste modelo inicial.
```

Cada veículo pertence a um proprietário e cada documento pertence a um veículo. Um proprietário pode não ter veículos, e um veículo pode não ter documentos. As chaves estrangeiras impedem excluir proprietários com veículos ou veículos com documentos vinculados.

### Regras iniciais

- Todos os IDs usam `BIGINT GENERATED ALWAYS AS IDENTITY` e chave primária. Não informe IDs nos inserts comuns.
- Login, CPF, CNH, placa, RENAVAM e chassi têm restrições de unicidade. CNH é opcional; vários proprietários podem ter CNH nula. Email não é exclusivo.
- Telefone, endereço, email, CNH e valor de venda são opcionais. Os demais campos são obrigatórios.
- CPF, CNH e RENAVAM são textos de 11 dígitos para preservar zeros à esquerda. As verificações de formato não validam dígitos verificadores nem autenticidade.
- Placa deve ser informada em maiúsculas e sem hífen, nos formatos antigo ou Mercosul. Chassi possui 17 caracteres e não aceita I, O ou Q.
- Quilometragem e valores não podem ser negativos. O ano de fabricação deve ser pelo menos 1886; o ano do modelo não pode ser menor que o de fabricação.
- Status permitido: `EM_ESTOQUE`, `RESERVADO`, `VENDIDO`.
- Tipo de documento permitido: `ATPV`, `CRLV`, `CRV`, `LAUDO_CAUTELAR`, `CONTRATO_COMPRA`, `CONTRATO_VENDA`, `NOTA_FISCAL`, `OUTRO`.
- Valor de venda representa inicialmente o valor anunciado; a regra para registrar o preço efetivo da venda será definida em uma próxima etapa.
- `data_upload` usa data e hora com fuso (`TIMESTAMP WITH TIME ZONE`) e recebe a data atual por padrão.
- `senha` deve guardar um hash gerado pela aplicação, nunca uma senha em texto puro. Não foi criado usuário de teste nesta etapa.

## Como executar

Tenha PostgreSQL instalado e um banco vazio chamado `revenda_veiculos`, com codificação UTF-8. Os scripts não criam o banco.

### Pelo pgAdmin ou DBeaver

1. Crie o banco `revenda_veiculos` e conecte-se a ele.
2. Execute o conteúdo completo de `database`.
3. Execute o conteúdo completo de `database`.
4. Execute `database`, se desejar carregar os exemplos.
5. Execute as consultas de `database`, juntas ou individualmente.

Os três primeiros scripts usam transações. Se houver erro, execute `ROLLBACK;` quando necessário, corrija a causa e repita apenas o script que falhou. Não prossiga para a próxima etapa enquanto houver erros.

### Pelo terminal com psql

Com `psql` disponível e o terminal aberto na pasta `revenda-veiculos`, crie o banco uma vez:

```sh
psql -U postgres -h localhost -d postgres -v ON_ERROR_STOP=1 -c "CREATE DATABASE revenda_veiculos ENCODING 'UTF8';"
```

Execute em ordem:

```sh
psql -U postgres -h localhost -d revenda_veiculos -v ON_ERROR_STOP=1 -f database/01_create_tables.sql
psql -U postgres -h localhost -d revenda_veiculos -v ON_ERROR_STOP=1 -f database/02_constraints.sql
psql -U postgres -h localhost -d revenda_veiculos -v ON_ERROR_STOP=1 -f database/03_inserts_teste.sql
psql -U postgres -h localhost -d revenda_veiculos -v ON_ERROR_STOP=1 -f database/consultas.sql
```

Substitua `postgres` pelo seu usuário se necessário. A senha será solicitada pela ferramenta; não coloque credenciais nos arquivos.

Os scripts de criação, constraints e exemplos foram preparados para execução única. Repeti-los em um banco já preenchido gera erros de objetos ou dados duplicados. Para preservar o histórico, futuras mudanças devem usar novos scripts de migração.

## Dados de teste e consultas

O exemplo cadastra João da Silva, um Toyota Corolla XEi 2020/2021 em estoque e o documento CRLV 2026. Todos os dados são fictícios. O caminho `uploads/documentos/ABC1D23/crlv-2026.pdf` é ilustrativo e o arquivo não é criado pelo SQL.

Os inserts obtêm os IDs gerados com `RETURNING`, sem depender de o primeiro registro ter ID 1. Após a execução, haverá um proprietário, um veículo e um documento de teste; a tabela `usuario` permanecerá vazia.

`consultas.sql` contém listagens básicas, veículos em estoque, um JOIN entre as três tabelas relacionadas, uma versão com LEFT JOIN para incluir veículos sem documentos e contagem de documentos por veículo.

## Próximos passos

1. Executar os scripts em um PostgreSQL local e conferir as consultas.
2. Criar o projeto Spring Boot com Spring Web, Spring Data JPA, PostgreSQL Driver e Thymeleaf.
3. Configurar a conexão usando variáveis de ambiente e um `.gitignore` antes de publicar no GitHub.
4. Mapear as entidades e relacionamentos JPA, mantendo `usuario` isolado nesta fase.
5. Implementar cadastros e validações de proprietários e veículos.
6. Implementar upload e armazenamento de documentos, com validação de arquivos.
7. Implementar autenticação com hash de senha e controle de acesso.
8. Criar páginas Thymeleaf, testes de integração e migrações versionadas do banco.

