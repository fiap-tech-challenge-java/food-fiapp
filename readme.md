# Projeto Food Fiapp

## 1. Descrição

O **Food Fiapp** é um sistema de back-end robusto para gestão de restaurantes, desenvolvido em Java com Spring Boot. O projeto foi concebido seguindo os princípios da **Arquitetura Limpa (Clean Architecture)** para garantir um código desacoplado, organizado, escalável e de fácil manutenção.

A plataforma permite que donos de restaurantes administrem os seus estabelecimentos, cardápios e operações, enquanto os clientes podem consultar informações e procurar restaurantes. O sistema foi projetado para ser uma solução compartilhada, reduzindo custos para os restaurantes e unificando a experiência do cliente.

## 2. Tecnologias Utilizadas

O projeto foi construído com um conjunto de tecnologias modernas e amplamente utilizadas no ecossistema Java:

| Categoria | Tecnologia | Versão |
| :--- | :--- | :--- |
| **Linguagem** | Java | 21 |
| **Framework** | Spring Boot | 3.5.5 |
| **Gestor de Dependências** | Maven | 3.9.9 |
| **Base de Dados (Produção)** | PostgreSQL | 16 |
| **Base de Dados (Testes)** | H2 (em memória) | |
| **Armazenamento de Ficheiros** | MinIO | latest |
| **Testes (Unitários e Integração)**| JUnit 5, Mockito, AssertJ | |
| **Cobertura de Código** | JaCoCo | 0.8.11 |
| **Análise de Arquitetura** | ArchUnit | 1.3.0 |
| **Documentação da API** | OpenAPI 3 (Springdoc) | 2.8.9 |
| **Mapeamento de Objetos** | MapStruct | 1.5.5.Final |
| **Conteinerização** | Docker & Docker Compose | |

## 3. Arquitetura do Projeto

O projeto adota a **Arquitetura Limpa (Clean Architecture)**, que organiza o código em camadas concêntricas. O princípio fundamental é que as dependências do código-fonte apontam sempre para o interior, protegendo as regras de negócio de detalhes de implementação externos como frameworks, bases de dados e interfaces de utilizador.

A estrutura de pacotes reflete essa filosofia:

- `com.fiap.foodfiapp.core.domain`: A camada mais interna (núcleo). Contém as **entidades de negócio** (ex: `User`, `Restaurant`), as **regras de negócio** e as **interfaces de portos** (ex: `UserRepository`). Esta camada é pura e não depende de nenhum framework externo.
- `com.fiap.foodfiapp.core.application`: A camada de aplicação. Contém os **casos de uso** (Use Cases, ex: `CreateUserUseCase`), que orquestram o fluxo de dados e a lógica para executar as funcionalidades do sistema. Depende apenas da camada de domínio.
- `com.fiap.foodfiapp.infrastructure`: A camada mais externa. Contém os detalhes de implementação que ligam o núcleo do sistema ao mundo exterior.
  - `infrastructure.rest`: Controladores da API REST e DTOs.
  - `infrastructure.persistence`: Implementações concretas dos repositórios definidos na camada de domínio, utilizando Spring Data JPA.
  - `infrastructure.config`: Configurações do Spring, como a injeção de dependências dos casos de uso.
  - `infrastructure.security`: Configurações de segurança.
  - `infrastructure.storage`: Implementação do cliente de armazenamento de objetos (MinIO).

Para garantir a conformidade com esta arquitetura, o projeto inclui testes com **ArchUnit** (`CleanArchitectureTest.java`) que validam as regras de dependência entre as camadas a cada build.

## 4. Como Executar o Projeto

A maneira mais simples e recomendada de executar o projeto é utilizando Docker e Docker Compose. Isto garante que toda a infraestrutura (aplicação, base de dados e armazenamento de ficheiros) seja configurada e executada de forma integrada.

### Pré-requisitos

- Docker
- Docker Compose

### Passo a Passo para Execução

1. **Clone o Repositório**

    ```bash
    git clone <url-do-seu-repositorio>
    cd food-fiapp
    ```

2. **Crie o Ficheiro de Ambiente (`.env`)**
    Na raiz do projeto, crie um ficheiro chamado `.env` a partir do `example.env` como base.

    ```dotenv
    # Spring
    SPRING_PROFILE=local
    SHOW_SQL=true

    # JWT
    JWT_SECRET=yX56ambwC9AM8QkQzYOpHr0wYviQizzneZBtbeO+IfY=
    JWT_EXPIRATION_MS=604800000

    # Banco de Dados (usado pelo Docker Compose)
    DB_URL=jdbc:postgresql://foodfiapp_postgres:5432/foodfiapp
    DB_USERNAME=postgres
    DB_PASSWORD=postgres
    DB_PORT=5432
    DB_NAME=foodfiapp

    # MinIO (usado pelo Docker Compose)
    MINIO_ENDPOINT=http://minio:9000
    MINIO_ACCESS_KEY=minioadmin
    MINIO_SECRET_KEY=minioadmin
    MINIO_BUCKET=menu-items
    MINIO_PUBLIC_ENDPOINT=http://localhost:9000
    ```

3. **Suba os Contentores**
    Execute o seguinte comando no terminal, a partir da raiz do projeto:

    ```bash
    docker-compose up --build
    ```

    Este comando irá construir a imagem Docker da aplicação, iniciar os contentores (PostgreSQL, MinIO) e executar a aplicação, que estará disponível em `http://localhost:8080`.

## 5. Documentação da API (Swagger)

A API do projeto é documentada utilizando a especificação **OpenAPI 3**. Uma interface interativa (Swagger UI) para explorar e testar os endpoints é gerada automaticamente.

### Como Aceder

Depois de iniciar a aplicação, aceda ao seguinte URL no seu navegador:

[**http://localhost:8080/api/v1/swagger-ui/index.html**](http://localhost:8080/api/v1/swagger-ui/index.html)

## 6. Testando a API com Postman

Pode importar a coleção de endpoints do Food Fiapp diretamente no Postman a partir da especificação OpenAPI.

### Passo a Passo para Testar

1. **Inicie a Aplicação**
    Certifique-se de que a aplicação está a ser executada.

2. **Abra o Postman e Importe a Coleção**
    - No Postman, clique em **"Import"** > **"Link"**.
    - Cole o URL da especificação OpenAPI: `http://localhost:8080/api/v1/v3/api-docs`
    - Siga as instruções para importar. O Postman criará a coleção "Food FiApp" com todos os endpoints.

> **Nota sobre Autenticação**: Para endpoints que exigem autenticação, primeiro obtenha um token JWT através do endpoint `POST /auth/login`. Depois, nas requisições protegidas, adicione o token no cabeçalho de autorização como "Bearer Token".

## 7. Testes e Qualidade de Código

A qualidade do código é uma prioridade neste projeto.

### Tipos de Teste

- **Testes Unitários**: Focados em testar pequenas unidades de lógica de forma isolada (JUnit 5 e Mockito).
- **Testes de Integração**: Verificam a interação entre diferentes componentes do sistema (controladores, casos de uso e base de dados em memória H2).
- **Testes de Arquitetura**: Garantem que as regras da Arquitetura Limpa são respeitadas (ArchUnit).

### Como Executar os Testes

Para executar todos os testes e gerar um relatório de cobertura de código, utilize o Maven Wrapper:

```bash
./mvnw clean verify
```

### Cobertura de Código

O projeto utiliza o **JaCoCo** para medir a cobertura de testes e está configurado no `pom.xml` para **falhar o build** se a cobertura de linhas for inferior a **90%**.

O relatório de cobertura detalhado pode ser encontrado em `target/site/jacoco/index.html` após executar o comando acima.
