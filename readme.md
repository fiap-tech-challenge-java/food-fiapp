# Projeto Food Fiapp

Este projeto é um back-end desenvolvido em Java com Spring Boot, seguindo os princípios da Arquitetura Limpa (Clean Architecture). O objetivo é demonstrar uma estrutura de projeto organizada, desacoplada e de fácil manutenção.

## Tecnologias

* **Linguagem**: Java 21
* **Framework**: Spring Boot 3.5.5
* **Gestor de Dependências**: Maven
* **Base de Dados (local)**: PostgreSQL
* **Base de Dados (testes)**: H2 (banco de dados em memória)
* **Teste de Unidade**: JUnit 5, AssertJ, Mockito
* **Cobertura de Código**: JaCoCo
* **Análise de Código Estático**: ArchUnit

## Arquitetura do Projeto

O projeto segue a **Arquitetura Limpa**, um padrão que separa a aplicação em camadas concêntricas. O foco é manter as regras de negócio independentes de frameworks, bancos de dados, interfaces e outras agências externas. As dependências do código-fonte sempre apontam para dentro, da camada externa para a interna.

A estrutura de pacotes reflete essa arquitetura:

* `src/main/java/com/fiap/foodfiapp/domain`: A camada mais interna. Contém as entidades de negócio (`User`) e os portos (interfaces de repositório, como `UserRepository`). **É a camada principal e não tem dependências de outras camadas.**
* `src/main/java/com/fiap/foodfiapp/application`: A camada de aplicação. Contém os casos de uso (`CreateUserUseCase`), que orquestram a lógica de negócio. Depende da camada de `domain` através de interfaces de gateway (`UserRepositoryGateway`).
* `src/main/java/com/fiap/foodfiapp/infrastructure`: A camada de infraestrutura. É a camada mais externa e lida com os detalhes de implementação, como a base de dados (`persistence`) e a API REST (`rest`). Depende das camadas `application` e `domain`.

## Como Executar o Projeto

### Pré-requisitos

* Java 21 JDK
* Maven
* Docker (opcional, para rodar o PostgreSQL localmente)

### 1. Configuração do Banco de Dados

O projeto está configurado para usar o PostgreSQL. As configurações de conexão estão no arquivo `.env`. Você pode usar um container Docker para iniciar a base de dados:

```bash
docker run --name food-fiapp-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgrespass -e POSTGRES_DB=foodfiapp -p 5432:5432 -d postgres
````

### 2\. Configuração do Ambiente

Crie um arquivo `.env` na raiz do projeto, com as seguintes variáveis de ambiente:

```
# Spring
SPRING_PROFILE=local
SHOW_SQL=true

# JWT
JWT_SECRET=yX56ambwC9AM8QkQzYOpHr0wYviQizzneZBtbeO+IfY=
JWT_EXPIRATION_MS=604800000

# Banco de Dados
DB_URL=jdbc:postgresql://localhost:5432/foodfiapp
DB_USERNAME=postgres
DB_PASSWORD=postgrespass
DB_PORT=5432
DB_NAME=foodfiapp
```

### 3\. Execução

Use o Maven Wrapper para iniciar a aplicação a partir do terminal.

```bash
./mvnw spring-boot:run
```

A aplicação será executada em `http://localhost:8080/`.

## Endpoints da API

* **`POST /api/v1/users`**
    * **Descrição**: Cria um novo utilizador.
    * **Corpo da Requisição**:
  <!-- end list -->
  ```json
  {
    "name": "Nome do Usuário",
    "email": "usuario@exemplo.com",
    "password": "senha"
  }
  ```
    * **Respostas**:
        * `201 CREATED`: Retorna os detalhes do utilizador criado, exceto a senha.
        * `409 CONFLICT`: Se já existir um utilizador com o mesmo email.

## Como Executar os Testes

Os testes de unidade garantem a funcionalidade da aplicação. O projeto utiliza o plugin JaCoCo para verificar a cobertura de código, e uma regra no `pom.xml` faz a compilação falhar se a cobertura for inferior a 90%.

Para rodar todos os testes e gerar um relatório de cobertura:

```bash
./mvnw clean verify
```

Além disso, o projeto inclui testes de arquitetura com o ArchUnit 
para garantir que as regras da Arquitetura Limpa sejam respeitadas.

```
