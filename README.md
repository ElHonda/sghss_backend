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

- POST `/api/auth/register` - Registro de novo usuário
  ```json
  {
    "nome": "Nome do Usuário",
    "email": "usuario@email.com",
    "senha": "senha123",
    "role": "ADMINISTRADOR"
  }
  ```

- POST `/api/auth/login` - Login de usuário
  ```json
  {
    "email": "usuario@email.com",
    "senha": "senha123"
  }
  ```

### Rotas de Teste

- GET `/api/admin/teste` - Rota de teste para administradores
- GET `/api/profissional/teste` - Rota de teste para profissionais
- GET `/api/paciente/teste` - Rota de teste para pacientes

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

Para executar os testes:
```bash
./mvnw test
``` 