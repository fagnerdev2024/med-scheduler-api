# Med Scheduler API

API REST para gestão de médicos, pacientes e agendamento de consultas.

Desenvolvida com **Kotlin**, **Spring Boot 3.4**, **JDK 21**, **Gradle 8.11** e arquitetura **hexagonal**, pronta para rodar localmente e ser deployada em múltiplos ambientes (dev, homol, prod) com **Render** e banco de dados **PostgreSQL** na **Neon**.

---

## Stack

| Camada | Tecnologia |
|--------|------------|
| Linguagem | Kotlin 2.0.21 |
| JDK | 21 |
| Build | Gradle 8.11.1 |
| Framework | Spring Boot 3.4.0 |
| ORM / Migrations | Spring Data JPA + Flyway |
| Banco de dados | PostgreSQL (produção) / PostgreSQL local via Docker |
| Segurança | Spring Security + JWT |
| Documentação | SpringDoc OpenAPI (Swagger UI) |
| Testes | JUnit 5 |
| Cloud | Render (Docker) |
| Banco na nuvem | Neon (PostgreSQL gerenciado) |
| CI/CD | GitHub Actions |

---

## Arquitetura

O projeto segue uma estrutura em camadas inspirada na arquitetura hexagonal:

```
com.med.scheduler
├── domain        # entidades, regras de negócio e interfaces (ports)
├── application   # casos de uso e DTOs
├── infrastructure # adapters, controllers, persistência, configurações
```

### Entidades principais

- **Medico**: cadastro de médicos com especialidade, CRM e endereço.
- **Paciente**: cadastro de pacientes com CPF e endereço.
- **Consulta**: agendamento e cancelamento de consultas, vinculando médico e paciente.
- **Usuario**: autenticação para acesso aos endpoints protegidos.

---

## Ambientes

A aplicação utiliza profiles do Spring Boot para separar os ambientes:

| Profile | Branch | Banco | Propósito |
|---------|--------|-------|-----------|
| `local` | sua máquina | PostgreSQL via Docker | desenvolvimento local |
| `dev` | `develop` | Neon (dev) | desenvolvimento na nuvem |
| `homol` | `release` | Neon (homol) | homologação |
| `prod` | `main` | Neon (prod) | produção |

---

## Estratégia de branches

O repositório segue o fluxo Git Flow simplificado:

```
feature/*  -> develop -> release -> main
```

1. Uma `feature/*` é aberta para `develop`.
2. Ao mergear em `develop`, o GitHub Actions abre automaticamente um PR `develop` -> `release`.
3. Ao mergear em `release`, o GitHub Actions abre automaticamente um PR `release` -> `main`.

Os serviços no Render fazem deploy a partir das branches correspondentes.

---

## Como rodar localmente

### Pré-requisitos

- JDK 21
- Docker e Docker Compose

### Passos

1. Clone o repositório e acesse a pasta:

```bash
git clone https://github.com/fagnerdev2024/med-scheduler-api.git
cd med-scheduler-api
```

2. Suba o banco de dados local:

```bash
docker compose up -d
```

3. Rode a aplicação com o profile `local`:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

A aplicação estará disponível em `http://localhost:8080`.

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

### Usuário de teste local

- **login:** `local@example.com`
- **senha:** `password`

---

## Como rodar na nuvem

### 1. Criar o banco no Neon

1. Acesse https://console.neon.tech e crie um projeto.
2. Copie a string de conexão JDBC/PostgreSQL.

### 2. Deploy no Render

A configuração de deploy está em `render.yaml`, com três serviços: `dev`, `homol` e `prod`.

1. No Render, clique em **New + > Blueprint**.
2. Conecte o repositório `fagnerdev2024/med-scheduler-api`.
3. Preencha as variáveis de ambiente para cada serviço:

| Variável | Exemplo |
|----------|---------|
| `SPRING_PROFILES_ACTIVE` | `dev` |
| `DATASOURCE_URL` | `jdbc:postgresql://<host>.neon.tech:5432/<banco>?sslmode=require` |
| `DATASOURCE_USERNAME` | `neondb_owner` |
| `DATASOURCE_PASSWORD` | `<senha do Neon>` |
| `JWT_SECRET` | `<string secreta>` |

### 3. Usuário de teste em dev

- **login:** `dev@example.com`
- **senha:** `password`

---

## Build e testes

Para compilar e rodar os testes:

```bash
./gradlew build
```

---

## Endpoints principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/login` | autenticação e geração de token JWT |
| POST | `/medicos` | cadastrar médico |
| GET | `/medicos` | listar médicos |
| POST | `/pacientes` | cadastrar paciente |
| GET | `/pacientes` | listar pacientes |
| POST | `/consultas` | agendar consulta |
| GET | `/consultas` | listar consultas |
| DELETE | `/consultas` | cancelar consulta |

---

## CI/CD

O repositório possui dois workflows do GitHub Actions:

- **CI**: roda `./gradlew build` em toda `push` e `pull request` nas branches `main`, `develop` e `release`.
- **Promote**: abre automaticamente PRs de `develop` para `release` e de `release` para `main`.

---

## Estrutura de migrations

- `db/migration-postgresql`: migrations base para PostgreSQL.
- `db/migration-local`: seed de usuário para testes locais.
- `db/migration-dev`: seed de usuário para ambiente dev.
- `db/migration`: migrations legado originais (MySQL).

---

## Contato

Projeto de estudo e treinamento de ambientes (local, dev, homol e prod) com Render, Neon e GitHub Actions.
