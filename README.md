# JPD School-Student Registration API

A secured RESTful backend API for managing schools and students, built with **Spring Boot 3**
and **PostgreSQL**. It provides full CRUD operations, stateless **JWT authentication** with
role-based users, request validation, pagination, and centralized error handling — backed by a
suite of unit and integration tests.

> Domain model: a school has many students; each student belongs to exactly one school.

---

## Features

- **Full CRUD REST API** for `School` and `Student` resources
- **Stateless JWT authentication** — register / login with BCrypt-hashed passwords
- **Role-based users** (`ROLE_USER`, `ROLE_ADMIN`) via Spring Security
- **Public reads, protected writes** — anyone can read; create / update / delete require a valid token
- **Request validation** with Jakarta Bean Validation
- **Pagination** on list endpoints
- **Centralized exception handling** with consistent, structured JSON error responses
- **DTO layer** — JPA entities are never exposed directly in requests or responses
- **25 automated tests** — Mockito unit tests + Spring MockMvc integration tests
- **API versioning** under `/api/v1`

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3.5.0 — Web, Data JPA, Security, Validation |
| Persistence | Spring Data JPA / Hibernate, PostgreSQL |
| Authentication | Spring Security, JSON Web Tokens (`jjwt` 0.12.6) |
| Build | Maven (wrapper included) |
| Database (dev) | PostgreSQL via Docker |
| Testing | JUnit 5, Mockito, Spring MockMvc, H2 (in-memory) |
| Utilities | Lombok |

---

## Project Structure

```
src/main/java/com/jpd/registration
├── RegistrationApplication.java
├── config/        SecurityConfig — Spring Security filter chain, password encoder
├── controller/    AuthController, SchoolController, StudentController
├── service/       AuthService, SchoolService, StudentService, UserDetailsServiceImpl
├── repository/    SchoolRepository, StudentRepository, UserRepository (Spring Data JPA)
├── model/         School, Student, User, Role — JPA entities
├── payload/       Request DTOs (SchoolPayload, StudentPayload)
│   ├── auth/      RegisterRequest, LoginRequest, AuthResponse
│   └── response/  SchoolResponse, StudentResponse, PageResponse
├── security/      JwtService, JwtAuthFilter — JWT generation & validation
└── exception/     ApiExceptionHandler, ResourceNotFoundException
```

The codebase follows a standard **controller → service → repository** layering: controllers
handle HTTP, services hold business logic, repositories handle persistence, and DTOs keep the
API contract separate from the JPA entities.

---

## API Endpoints

All endpoints are versioned under `/api/v1`.

### Authentication — `/api/v1/auth` (public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/auth/register` | Create an account; returns a JWT |
| `POST` | `/api/v1/auth/login` | Authenticate; returns a JWT |

### Schools — `/api/v1/schools`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/v1/schools` | Public | List schools (paginated) |
| `GET` | `/api/v1/schools/{id}` | Public | Get a school by id |
| `POST` | `/api/v1/schools` | Required | Create a school |
| `PUT` | `/api/v1/schools/{id}` | Required | Update a school |
| `DELETE` | `/api/v1/schools/{id}` | Required | Delete a school (blocked if it still has students) |

### Students — `/api/v1/students`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/v1/students` | Public | List students (paginated) |
| `GET` | `/api/v1/students/{id}` | Public | Get a student by id |
| `POST` | `/api/v1/students` | Required | Create a student |
| `PUT` | `/api/v1/students/{id}` | Required | Update a student |
| `DELETE` | `/api/v1/students/{id}` | Required | Delete a student |

> Write operations (`POST`, `PUT`, `DELETE`) require a valid JWT. Requests without one
> receive `403 Forbidden`.

---

## Authentication

The API uses **stateless JWT authentication** — no server-side session is stored.

1. Call `/api/v1/auth/register` or `/api/v1/auth/login` to receive a token.
2. Send the token on protected requests via the header `Authorization: Bearer <token>`.
3. `JwtAuthFilter` validates the token on every request and sets the security context.

New accounts are assigned `ROLE_USER`. Passwords are hashed with BCrypt and are never stored
or returned in plain text.

**Register**

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"jpd","email":"jpd@example.com","password":"password123"}'
```

Response:

```json
{ "token": "<jwt>", "tokenType": "Bearer", "username": "jpd", "role": "ROLE_USER" }
```

**Use the token on a protected request**

```bash
curl -X POST http://localhost:8080/api/v1/schools \
  -H "Authorization: Bearer <jwt>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Harvard University"}'
```

---

## Getting Started

### Prerequisites

- Java 17+
- Docker (to run PostgreSQL)
- Git
- Maven — optional; the project includes the `mvnw` wrapper

### 1. Clone the repository

```bash
git clone https://github.com/JodPdo/JPD-school-student-registration-system.git
cd JPD-school-student-registration-system/registration
```

### 2. Start PostgreSQL with Docker

```bash
docker run --name jpd-postgres \
  -e POSTGRES_USER=jpd \
  -e POSTGRES_PASSWORD=123456 \
  -e POSTGRES_DB=jpd_db \
  -p 5432:5432 \
  -d postgres:17.5
```

This starts a PostgreSQL server on `localhost:5432`.

### 3. Configure `src/main/resources/application.properties`

```properties
spring.application.name=registration

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/jpd_db
spring.datasource.username=jpd
spring.datasource.password=123456

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false

# JWT — secret must be Base64-encoded and decode to at least 256 bits (32 bytes)
jwt.secret=<your-base64-encoded-secret>
jwt.expiration=86400000
```

Generate a JWT secret with:

```bash
openssl rand -base64 32
```

Hibernate creates the tables automatically on first start (`ddl-auto=update`).

### 4. Run the application

```bash
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`. You can also run `RegistrationApplication.java`
directly from your IDE.

---

## Data Model

`School` **1 ──< many** `Student` — each student belongs to one school.
`User` is a separate entity used only for authentication.

| Table | Key columns |
|-------|-------------|
| `schools` | `id`, `name` (unique), `created_at`, `updated_at` |
| `students` | `id`, `first_name`, `last_name`, `school_id` → `schools.id`, `created_at`, `updated_at` |
| `users` | `id`, `username` (unique), `email` (unique), `password` (BCrypt), `role`, `created_at` |

Entity IDs are auto-generated (`GenerationType.IDENTITY`); `created_at` / `updated_at` are
maintained automatically by JPA lifecycle callbacks (`@PrePersist` / `@PreUpdate`).

**Business rules**

- A student references its school by **name** (`schoolName`) on create / update; the school
  must already exist, otherwise the API responds `404 Not Found`.
- A school cannot be deleted while it still has enrolled students (`400 Bad Request`).
- Usernames and emails must be unique at registration.

---

## Error Handling

`ApiExceptionHandler` (`@ControllerAdvice`) returns consistent JSON for failures:

| Situation | Status | Response body |
|-----------|--------|---------------|
| Resource not found | `404` | `{ timestamp, status, message }` |
| Invalid argument / business-rule violation | `400` | `{ timestamp, status, message }` |
| Bean validation failure | `400` | `{ timestamp, status, message: "Validation failed", errors: { field: message } }` |

---

## Testing

The project includes **25 automated tests**:

- **Service unit tests** (`SchoolServiceTest`, `StudentServiceTest`) — business logic tested
  in isolation with Mockito.
- **Controller integration tests** (`SchoolControllerTest`) — full request/response flow with
  Spring MockMvc, exercising authentication, validation, and access control.
- **Context test** (`RegistrationApplicationTests`) — verifies the application context loads.

Run the suite with:

```bash
./mvnw test
```

Integration tests use an in-memory H2 database, so PostgreSQL is **not** required to run the
tests.

---

## Security Notes

The `jwt.secret` and database credentials shown above are intended for **local development
only**. For any shared or deployed environment, use strong, unique values and keep them out of
version control — for example via environment variables or an untracked configuration file.
