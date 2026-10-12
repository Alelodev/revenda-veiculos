# Revenda de Veículos

Sistema web desenvolvido para auxiliar no gerenciamento de uma revenda de veículos.

A aplicação permite cadastrar proprietários, veículos, usuários e documentos relacionados aos veículos, além de controlar o acesso aos dados de acordo com o usuário autenticado.

A versão atual representa a **v1.0 do projeto**, contendo o fluxo principal da aplicação funcionando de ponta a ponta.

---

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Spring Security
- Thymeleaf
- PostgreSQL
- BCrypt
- HTML
- JavaScript
- Maven
- Git / GitHub

---

# Funcionalidades da v1.0

## Autenticação

O sistema possui autenticação utilizando Spring Security.

Funcionalidades implementadas:

- login de usuário;
- logout;
- sessões autenticadas;
- senhas criptografadas com BCrypt;
- controle de acesso por perfil.

Existem dois perfis:

```text
ADMIN
USER
```

---

## Controle de acesso

### ADMIN

O administrador pode:

- acessar todos os veículos cadastrados;
- acessar documentos de todos os veículos;
- cadastrar novos usuários;
- cadastrar proprietários;
- cadastrar veículos;
- fazer upload de documentos;
- fazer download de documentos;
- excluir documentos;
- visualizar registros cadastrados por outros usuários.

Nesta versão, a alteração de um usuário de `USER` para `ADMIN` é feita diretamente no banco de dados.

---

### USER

O usuário comum pode:

- acessar o sistema;
- cadastrar proprietários;
- cadastrar veículos;
- visualizar somente os veículos cadastrados por ele;
- acessar documentos somente dos próprios veículos;
- enviar documentos;
- baixar documentos;
- excluir documentos permitidos.

A validação de acesso é feita também no backend.

Isso impede que um usuário tente acessar dados de outro usuário alterando manualmente o ID na URL.

Exemplo:

```text
USER A
   ↓
Veículos cadastrados pelo USER A


USER B
   ↓
Não consegue visualizar os veículos do USER A


ADMIN
   ↓
Visualiza todos
```

---

# Proprietários

O sistema permite cadastrar proprietários dos veículos.

Dados disponíveis:

- nome completo;
- CPF;
- telefone;
- endereço;
- e-mail;
- CNH.

Relacionamento:

```text
PROPRIETARIO
      1
      |
      |
      N
   VEICULO
```

Um proprietário pode possuir vários veículos.

---

# Veículos

O sistema permite cadastrar veículos e associá-los ao usuário que realizou o cadastro.

Cada veículo possui:

- proprietário;
- usuário responsável pelo cadastro;
- marca;
- modelo;
- ano de fabricação;
- ano modelo;
- placa;
- cor;
- tipo de combustível;
- RENAVAM;
- chassi;
- quilometragem;
- valor de compra;
- valor de venda;
- status.

---

## Status do veículo

Os status disponíveis são:

```text
EM_ESTOQUE
RESERVADO
VENDIDO
```

---

## Marca

O formulário possui algumas marcas predefinidas.

Também existe a opção:

```text
Outra
```

Ao selecionar essa opção, o sistema permite informar manualmente uma marca.

---

## Cor

O sistema possui cores predefinidas, como:

- Branco
- Preto
- Prata
- Cinza
- Vermelho
- Azul
- Verde
- Amarelo
- Marrom
- Bege
- Laranja
- Roxo

Também existe a opção:

```text
Outra
```

permitindo informar uma cor diferente.

---

## Tipo de combustível

As opções disponíveis incluem:

- Gasolina
- Etanol
- Flex
- Diesel
- GNV
- Elétrico
- Híbrido

Também existe:

```text
Outro
```

permitindo informar manualmente outro tipo de combustível.

---

## Placa

A placa possui restrição de unicidade no banco de dados.

O sistema verifica antes do cadastro se já existe um veículo com a mesma placa.

Exemplo:

```text
ABC1D23
```

A aplicação também normaliza a placa antes de salvar:

```text
 abc1d23
     ↓
ABC1D23
```

Isso evita duplicidade causada por letras minúsculas ou espaços.

---

# Documentos

Cada veículo pode possuir vários documentos associados.

Tipos atualmente suportados:

- ATPV
- CRLV
- CRV
- Laudo cautelar
- Contrato de compra
- Contrato de venda
- Nota fiscal
- Outro

O sistema permite:

- upload de arquivos;
- listagem dos documentos;
- download;
- exclusão do registro;
- controle de acesso de acordo com o usuário responsável pelo veículo.

---

## Armazenamento dos arquivos

Os arquivos não são armazenados diretamente no PostgreSQL.

O banco armazena informações sobre o documento e o caminho do arquivo.

Os arquivos ficam organizados aproximadamente assim:

```text
uploads/
└── veiculos/
    └── {idVeiculo}/
        └── arquivo.pdf
```

Cada documento no banco possui:

- veículo;
- nome;
- tipo;
- caminho do arquivo;
- data de upload.

---

# Segurança

## Spring Security

A aplicação utiliza Spring Security para controlar autenticação e autorização.

Fluxo:

```text
Usuário
   ↓
Login
   ↓
Spring Security
   ↓
Banco de dados
   ↓
Verificação da senha
   ↓
Sessão autenticada
```

---

## BCrypt

As senhas nunca devem ser armazenadas em texto puro.

Antes de salvar:

```text
senha digitada
      ↓
BCrypt
      ↓
hash
      ↓
PostgreSQL
```

O mesmo processo é utilizado ao atualizar uma senha.

A senha também é ignorada nas respostas JSON da API.

---

## Perfis

A tabela `usuario` possui:

```text
USER
ADMIN
```

O Spring Security utiliza o perfil salvo no banco para determinar as permissões.

---

## CSRF

Os formulários web protegidos utilizam token CSRF do Spring Security.

Isso protege operações `POST` como:

- cadastro de usuário;
- cadastro de proprietário;
- cadastro de veículo;
- upload de documento;
- exclusão;
- logout.

---

# Modelo de dados

## Usuario

```text
usuario (
    id_usuario PK,
    login UNIQUE,
    senha,
    perfil
)
```

---

## Proprietario

```text
proprietario (
    id_proprietario PK,
    nome_completo,
    cpf UNIQUE,
    telefone,
    endereco,
    email,
    cnh
)
```

---

## Veiculo

```text
veiculo (
    id_veiculo PK,

    id_proprietario FK,
    id_usuario FK,

    marca,
    modelo,

    ano_fabricacao,
    ano_modelo,

    placa UNIQUE,

    cor,
    tipo_combustivel,

    renavam,
    chassi,

    quilometragem,

    valor_compra,
    valor_venda,

    status
)
```

---

## Documento

```text
documento (
    id_documento PK,
    id_veiculo FK,

    nome,
    tipo_documento,
    caminho_arquivo,
    data_upload
)
```

---

# Relacionamentos

```text
USUARIO
   1
   |
   |
   N
VEICULO


PROPRIETARIO
     1
     |
     |
     N
  VEICULO


VEICULO
   1
   |
   |
   N
DOCUMENTO
```

Um veículo possui:

```text
1 proprietário
1 usuário responsável pelo cadastro
N documentos
```

---

# Arquitetura

A aplicação utiliza uma organização em camadas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

---

## Estrutura aproximada

```text
src/main/java/br/com/revenda/

├── config/
│   └── SecurityConfig.java
│
├── controller/
│   ├── HomeController.java
│   ├── UsuarioController.java
│   ├── ProprietarioController.java
│   ├── VeiculoController.java
│   └── DocumentoController.java
│
├── exception/
│   ├── DadosInvalidosException.java
│   ├── RecursoNaoEncontradoException.java
│   └── GlobalExceptionHandler.java
│
├── model/
│   ├── Usuario.java
│   ├── Proprietario.java
│   ├── Veiculo.java
│   └── Documento.java
│
├── repository/
│   ├── UsuarioRepository.java
│   ├── ProprietarioRepository.java
│   ├── VeiculoRepository.java
│   └── DocumentoRepository.java
│
├── security/
│   └── ServicoDeUsuarioCustomizado.java
│
└── service/
    ├── UsuarioService.java
    ├── ProprietarioService.java
    ├── VeiculoService.java
    └── DocumentoService.java
```

---

# Templates

As páginas Thymeleaf ficam em:

```text
src/main/resources/templates/
```

Entre elas:

```text
login.html

index.html

veiculos.html
cadastro-veiculo.html

proprietarios.html
cadastro-proprietario.html

cadastro-usuario.html

documentos-veiculo.html
```

---

# Banco de dados

O projeto utiliza:

```text
PostgreSQL
```

Os scripts SQL ficam em:

```text
database/
├── 00_create_database.sql
├── 01_create_tables.sql
├── 02_constraints.sql
├── 03_inserts_teste.sql
└── consultas.sql
```

---

## Função de cada script

### `00_create_database.sql`

Cria o banco:

```text
revenda_veiculos
```

---

### `01_create_tables.sql`

Cria as tabelas:

```text
usuario
proprietario
veiculo
documento
```

---

### `02_constraints.sql`

Cria:

- constraints;
- validações;
- regras de unicidade;
- regras de status;
- índices.

---

### `03_inserts_teste.sql`

Insere dados fictícios para testes.

Como todo veículo precisa possuir um usuário responsável, é necessário existir pelo menos um usuário no sistema antes da execução desse script.

---

### `consultas.sql`

Contém consultas úteis para estudo e verificação do banco.

Entre elas:

- usuários;
- proprietários;
- veículos;
- documentos;
- veículos em estoque;
- veículos por usuário;
- proprietário + usuário + veículo;
- quantidade de documentos;
- quantidade de veículos por usuário.

---

# Ordem para criar o banco

Execute os scripts nesta ordem:

```text
00_create_database.sql
        ↓
01_create_tables.sql
        ↓
02_constraints.sql
        ↓
03_inserts_teste.sql  (opcional)
        ↓
consultas.sql         (opcional)
```

---

# Como executar

## Requisitos

Tenha instalado:

- Java
- Maven
- PostgreSQL
- Git

Uma IDE como IntelliJ IDEA também pode ser utilizada.

---

## 1. Clone o repositório

```bash
git clone https://github.com/Alelodev/revenda-veiculos.git
```

Entre na pasta:

```bash
cd revenda-veiculos
```

---

## 2. Crie o banco de dados

Primeiro execute:

```text
database/00_create_database.sql
```

O script deve ser executado conectado ao banco padrão:

```text
postgres
```

Exemplo com `psql`:

```bash
psql -U postgres -h localhost -d postgres -f database/00_create_database.sql
```

Isso criará:

```text
revenda_veiculos
```

---

## 3. Conecte ao banco novo

Depois da criação, utilize:

```text
revenda_veiculos
```

para executar os demais scripts.

---

## 4. Crie as tabelas

```bash
psql -U postgres -h localhost -d revenda_veiculos -v ON_ERROR_STOP=1 -f database/01_create_tables.sql
```

---

## 5. Crie constraints e índices

```bash
psql -U postgres -h localhost -d revenda_veiculos -v ON_ERROR_STOP=1 -f database/02_constraints.sql
```

---

## 6. Execute dados de teste — opcional

Primeiro crie pelo menos um usuário através da aplicação.

Depois:

```bash
psql -U postgres -h localhost -d revenda_veiculos -v ON_ERROR_STOP=1 -f database/03_inserts_teste.sql
```

---

## 7. Execute as consultas — opcional

```bash
psql -U postgres -h localhost -d revenda_veiculos -v ON_ERROR_STOP=1 -f database/consultas.sql
```

---

# Configuração do Spring Boot

Configure a conexão com:

```text
revenda_veiculos
```

As credenciais do PostgreSQL não devem ser publicadas no GitHub.

Utilize variáveis de ambiente para informações sensíveis.

Exemplo conceitual:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/revenda_veiculos
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

---

## `.env`

O arquivo com credenciais não deve ser enviado para o GitHub.

Utilize:

```text
.env
```

no `.gitignore`.

Pode existir um arquivo:

```text
.env.exemplo
```

sem dados sensíveis para mostrar quais variáveis são necessárias.

---

# Executando a aplicação

Pelo IntelliJ IDEA, execute a classe principal do Spring Boot.

Ou utilize Maven:

```bash
mvn spring-boot:run
```

---

## Acesso

Com a aplicação rodando:

```text
http://localhost:8080
```

A primeira tela será:

```text
Login
```

---

# Fluxo da aplicação

```text
/login
   ↓
/principal
   ↓
Menu principal
   ├── Veículos
   ├── Proprietários
   ├── Cadastrar veículo
   ├── Cadastrar proprietário
   └── Cadastrar usuário (ADMIN)
```

---

## Fluxo de veículo

```text
Usuário autenticado
        ↓
Cadastrar veículo
        ↓
Spring identifica o usuário
        ↓
veiculo.id_usuario
        ↓
PostgreSQL
```

---

## Consulta dos veículos

```text
ADMIN
  ↓
Todos os veículos
```

```text
USER
  ↓
Somente veículos cadastrados pelo próprio usuário
```

---

## Fluxo dos documentos

```text
Veículo
   ↓
Documentos
   ↓
Upload
   ↓
Arquivo salvo no servidor
   ↓
Caminho salvo no PostgreSQL
```

---

# Tratamento de erros

A aplicação possui tratamento centralizado de exceções.

Classe:

```text
GlobalExceptionHandler
```

Entre os erros tratados estão:

- recurso não encontrado;
- dados inválidos;
- erros de validação.

---

## Exemplo: placa duplicada

```text
Usuário cadastra veículo
        ↓
VeiculoService
        ↓
Verifica placa
        ↓
Placa já existe?
        ↓
SIM
        ↓
DadosInvalidosException
        ↓
HTTP 400
```

Mensagem:

```text
Já existe um veículo cadastrado com essa placa.
```

---

# API

Além das páginas Thymeleaf, alguns recursos também possuem endpoints REST.

Exemplos:

```text
/usuarios
/proprietarios
/veiculos
/documentos
```

Os endpoints protegidos utilizam o usuário autenticado para verificar permissões.

Um `USER` não pode acessar veículos ou documentos pertencentes a outro usuário.

As operações de usuários são restritas ao perfil:

```text
ADMIN
```

---

# Versão 1.0

A versão **1.0** contém o núcleo funcional do sistema.

Funcionalidades principais concluídas:

- banco PostgreSQL;
- entidades JPA;
- repositories;
- services;
- controllers;
- interface Thymeleaf;
- autenticação;
- Spring Security;
- BCrypt;
- perfis USER e ADMIN;
- controle de acesso;
- proprietários;
- veículos;
- vínculo veículo/usuário;
- upload de documentos;
- download de documentos;
- exclusão de documentos;
- proteção de documentos;
- validação de placa duplicada;
- logout;
- tratamento de exceções.

O sistema já pode ser utilizado como base funcional da aplicação de gerenciamento da revenda.

---

# Próximas versões

## v1.1

Possíveis melhorias:

- edição de veículos;
- edição de proprietários;
- tela de gerenciamento de usuários;
- busca por placa;
- pesquisa de veículos;
- filtros por status;
- paginação;
- mensagens de erro diretamente nas páginas;
- validações adicionais;
- melhoria visual;
- dashboard simples.

---

## v2.0

Funcionalidades planejadas:

- integração com API da Tabela FIPE;
- consulta automática de marca/modelo;
- consulta de valor FIPE;
- consulta de histórico do veículo;
- consulta de sinistro através de API;
- relatórios;
- dashboard;
- histórico de vendas;
- controle de lucro;
- informações financeiras da revenda;
- pesquisa avançada.

---

# Objetivo do projeto

O projeto foi criado para praticar e aplicar conhecimentos de Engenharia de Software utilizando um problema real de gerenciamento de veículos.

Entre os conceitos aplicados estão:

- orientação a objetos;
- arquitetura em camadas;
- APIs REST;
- MVC;
- bancos de dados relacionais;
- SQL;
- JPA;
- Hibernate;
- Spring Boot;
- Spring Security;
- autenticação;
- autorização;
- gerenciamento de arquivos;
- Git e GitHub.

---

# Autor

**Angelo Roberto**

Estudante de Engenharia de Software.

GitHub:

https://github.com/Alelodev