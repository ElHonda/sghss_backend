# SGHSS Backend

Backend do Sistema de Gestão de Saúde e Serviços Sociais (SGHSS).

## Requisitos

- Java 17
- Maven
- Docker e Docker Compose
- PostgreSQL 15

## Configuração do Ambiente

1. Clone o repositório:
    ```bash
    git clone [URL_DO_REPOSITORIO]
    cd sghss_backend
    ```

2. Configure as variáveis de ambiente:
   - Crie um arquivo `.env` na raiz do projeto
   - Adicione as seguintes variáveis:
     ```
     JWT_SECRET=sua_chave_secreta_aqui
     ```

3. Execute o projeto usando Docker Compose:
    ```bash
    docker compose up -d
    ```

## Endpoints da API

### Autenticação

- **POST** `/api/auth/login`  
  Login de usuário (Admin, Profissional ou Paciente)
  
  **Request Body:**
  ```json
  {
    "email": "usuario@email.com",
    "password": "senha123"
  }
  ```
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Login realizado com sucesso",
    "data": {
      "token": "<jwt_token>"
    }
  }
  ```

- **POST** `/api/auth/register`  
  Registro de novo administrador (apenas administradores podem registrar)
  
  **Headers:**
  - Authorization: Bearer <token>
  - Content-Type: application/json

  **Request Body:**
  ```json
  {
    "nome": "Nome do Usuário",
    "email": "usuario@email.com",
    "senha": "senha123",
    "role": "ADMINISTRADOR"
  }
  ```
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Usuário registrado com sucesso",
    "data": {
      "id": 2,
      "nome": "Nome do Usuário",
      "email": "usuario@email.com",
      "role": "ADMINISTRADOR"
    }
  }
  ```

### Público

- **GET** `/api/public/ping`  
  Verifica se a API está online
  
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Ping realizado com sucesso",
    "data": "pong"
  }
  ```

### Administrador

#### Teste
- **GET** `/api/admin/teste`  
  Teste de acesso para administradores
  
  **Headers:**
  - Authorization: Bearer <token>
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Acesso de administrador com sucesso",
    "data": "Olá, administrador!"
  }
  ```

#### Tipos de Profissional
- **POST** `/api/admin/tipos-profissional`  
  Cria um novo tipo de profissional
  
  **Headers:**
  - Authorization: Bearer <token>
  - Content-Type: application/json

  **Request Body:**
  ```json
  { "nome": "Fisioterapeuta" }
  ```
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Tipo de profissional criado com sucesso",
    "data": {
      "id": 1,
      "nome": "Fisioterapeuta"
    }
  }
  ```

- **GET** `/api/admin/tipos-profissional`  
  Lista todos os tipos de profissional
  
  **Headers:**
  - Authorization: Bearer <token>
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Tipos de profissional listados com sucesso",
    "data": [
      { "id": 1, "nome": "Fisioterapeuta" },
      { "id": 2, "nome": "Fonoaudiólogo" }
    ]
  }
  ```

- **GET** `/api/admin/tipos-profissional/{id}`  
  Busca tipo de profissional por ID
  
  **Headers:**
  - Authorization: Bearer <token>
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Tipo de profissional encontrado com sucesso",
    "data": { "id": 1, "nome": "Fisioterapeuta" }
  }
  ```
  (ou)
  ```json
  {
    "status": 404,
    "message": "Tipo de profissional não encontrado",
    "data": null
  }
  ```

- **PUT** `/api/admin/tipos-profissional/{id}`  
  Atualiza tipo de profissional
  
  **Headers:**
  - Authorization: Bearer <token>
  - Content-Type: application/json

  **Request Body:**
  ```json
  { "nome": "Fonoaudiólogo" }
  ```
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Tipo de profissional atualizado com sucesso",
    "data": { "id": 1, "nome": "Fonoaudiólogo" }
  }
  ```
  (ou)
  ```json
  {
    "status": 404,
    "message": "Tipo de profissional não encontrado",
    "data": null
  }
  ```

- **DELETE** `/api/admin/tipos-profissional/{id}`  
  Remove tipo de profissional
  
  **Headers:**
  - Authorization: Bearer <token>
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Tipo de profissional removido com sucesso",
    "data": null
  }
  ```
  (ou)
  ```json
  {
    "status": 404,
    "message": "Tipo de profissional não encontrado",
    "data": null
  }
  ```

#### Profissionais
- **POST** `/api/admin/profissionais`  
  Cria um novo profissional
  
  **Headers:**
  - Authorization: Bearer <token>
  - Content-Type: application/json

  **Request Body:**
  ```json
  {
    "nome": "Profissional Teste",
    "email": "profissional@teste.com",
    "senha": "senha123",
    "tipoProfissionalId": 1,
    "registroProfissional": "123456"
  }
  ```
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Profissional criado com sucesso",
    "data": {
      "id": 1,
      "nome": "Profissional Teste",
      "emailUsuario": "profissional@teste.com",
      "tipoProfissional": { "id": 1, "nome": "Fisioterapeuta" },
      "registroProfissional": "123456"
    }
  }
  ```

- **GET** `/api/admin/profissionais`  
  Lista todos os profissionais
  
  **Headers:**
  - Authorization: Bearer <token>
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Profissionais listados com sucesso",
    "data": [
      {
        "id": 1,
        "nome": "Profissional Teste",
        "emailUsuario": "profissional@teste.com",
        "tipoProfissional": { "id": 1, "nome": "Fisioterapeuta" },
        "registroProfissional": "123456"
      }
    ]
  }
  ```

- **GET** `/api/admin/profissionais/{id}`  
  Busca profissional por ID
  
  **Headers:**
  - Authorization: Bearer <token>
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Profissional encontrado com sucesso",
    "data": {
      "id": 1,
      "nome": "Profissional Teste",
      "emailUsuario": "profissional@teste.com",
      "tipoProfissional": { "id": 1, "nome": "Fisioterapeuta" },
      "registroProfissional": "123456"
    }
  }
  ```
  (ou)
  ```json
  {
    "status": 404,
    "message": "Profissional não encontrado",
    "data": null
  }
  ```

- **PUT** `/api/admin/profissionais/{id}`  
  Atualiza profissional
  
  **Headers:**
  - Authorization: Bearer <token>
  - Content-Type: application/json

  **Request Body:**
  ```json
  { "registroProfissional": "654321" }
  ```
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Profissional atualizado com sucesso",
    "data": {
      "id": 1,
      "nome": "Profissional Teste",
      "emailUsuario": "profissional@teste.com",
      "tipoProfissional": { "id": 1, "nome": "Fisioterapeuta" },
      "registroProfissional": "654321"
    }
  }
  ```
  (ou)
  ```json
  {
    "status": 404,
    "message": "Profissional não encontrado",
    "data": null
  }
  ```

- **DELETE** `/api/admin/profissionais/{id}`  
  Remove profissional
  
  **Headers:**
  - Authorization: Bearer <token>
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Profissional removido com sucesso",
    "data": null
  }
  ```
  (ou)
  ```json
  {
    "status": 404,
    "message": "Profissional não encontrado",
    "data": null
  }
  ```

#### Pacientes
- **POST** `/api/admin/pacientes`  
  Cria um novo paciente
  
  **Headers:**
  - Authorization: Bearer <token>
  - Content-Type: application/json

  **Request Body:**
  ```json
  {
    "nome": "Paciente Teste",
    "dataNascimento": "1990-01-01",
    "cpf": "12345678900",
    "telefone": "11999999999",
    "endereco": "Rua Exemplo, 123",
    "email": "paciente@teste.com",
    "senha": "senha123"
  }
  ```
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Paciente criado com sucesso",
    "data": {
      "id": 1,
      "nome": "Paciente Teste",
      "email": "paciente@teste.com"
    }
  }
  ```

- **GET** `/api/admin/pacientes`  
  Lista todos os pacientes
  
  **Headers:**
  - Authorization: Bearer <token>
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Pacientes listados com sucesso",
    "data": [
      {
        "id": 1,
        "nome": "Paciente Teste",
        "email": "paciente@teste.com"
      }
    ]
  }
  ```

- **GET** `/api/admin/pacientes/{id}`  
  Busca paciente por ID
  
  **Headers:**
  - Authorization: Bearer <token>
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Paciente encontrado com sucesso",
    "data": {
      "id": 1,
      "nome": "Paciente Teste",
      "email": "paciente@teste.com"
    }
  }
  ```
  (ou)
  ```json
  {
    "status": 404,
    "message": "Paciente não encontrado",
    "data": null
  }
  ```

- **PUT** `/api/admin/pacientes/{id}`  
  Atualiza paciente
  
  **Headers:**
  - Authorization: Bearer <token>
  - Content-Type: application/json

  **Request Body:**
  ```json
  {
    "nome": "Paciente Atualizado",
    "dataNascimento": "1991-02-02",
    "cpf": "98765432100",
    "telefone": "11888888888",
    "endereco": "Rua Nova, 456",
    "email": "paciente@teste.com",
    "senha": "senha123"
  }
  ```
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Paciente atualizado com sucesso",
    "data": {
      "id": 1,
      "nome": "Paciente Atualizado",
      "email": "paciente@teste.com"
    }
  }
  ```
  (ou)
  ```json
  {
    "status": 404,
    "message": "Paciente não encontrado",
    "data": null
  }
  ```

- **DELETE** `/api/admin/pacientes/{id}`  
  Remove paciente
  
  **Headers:**
  - Authorization: Bearer <token>
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Paciente removido com sucesso",
    "data": null
  }
  ```
  (ou)
  ```json
  {
    "status": 404,
    "message": "Paciente não encontrado",
    "data": null
  }
  ```

### Profissional

- **GET** `/api/profissional/teste`  
  Teste de acesso para profissionais
  
  **Headers:**
  - Authorization: Bearer <token>
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Acesso de profissional com sucesso",
    "data": "Olá, profissional!"
  }
  ```

### Paciente

- **GET** `/api/paciente/teste`  
  Teste de acesso para pacientes
  
  **Headers:**
  - Authorization: Bearer <token>
  **Response:**
  ```json
  {
    "status": 200,
    "message": "Acesso de paciente com sucesso",
    "data": "Olá, paciente!"
  }
  ```

### Observações
- Todos os endpoints (exceto `/public/ping` e `/auth/login`) exigem autenticação via JWT no header `Authorization: Bearer <token>`.
- Os exemplos de body e resposta podem variar conforme regras de negócio e validações.
- Para mais exemplos de fluxo (criação, login, uso de tokens), consulte a collection Postman `sghss-api.postman_collection.json` incluída no projeto.

## Níveis de Acesso

1. ADMINISTRADOR
   - Acesso total ao sistema
   - Gerenciamento de usuários
   - Configurações do sistema

2. PROFISSIONAL
   - Acesso às funcionalidades profissionais
   - Gerenciamento de pacientes
   - Registro de atendimentos

3. PACIENTE
   - Acesso às funcionalidades do paciente
   - Visualização de histórico
   - Agendamento de consultas

## Desenvolvimento

Para executar o projeto em modo de desenvolvimento:

1. Inicie o banco de dados:
   ```bash
   docker compose up -d db
   ```

2. Execute a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```

## Testes

1. Para executar os testes:
   ```bash
   ./mvnw test
   ``` 