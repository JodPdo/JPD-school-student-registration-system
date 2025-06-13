# JPD School and Student Registration System

This project is a backend development example using Spring Boot and PostgreSQL.
It sets up a basic registration system with two entities: `School` and `Student`.

---

## Getting Started

Follow the instructions below to run this project on your local machine.

---

## Prerequisites

- Java 17+
- Maven
- Docker (for running PostgreSQL)
- Git

---

## Tech Stack

- Spring Boot
- Docker
- PostgreSQL
- Git / GitHub
- VS Code

---

## Project Setup

### 1. Clone the Repository

```bash
git clone https://github.com/JpdPdo/JPD-school-student-registration-system.git
cd JPD-school-student-registration-system
git checkout -b main
```

---

### 2. Start PostgreSQL using Docker

First, pull the official PostgreSQL image:

```bash
docker pull postgres:17.5
```

Then, run a new container:

```bash
docker run --name jpd-postgres \
  -e POSTGRES_USER=jpd \
  -e POSTGRES_PASSWORD=123456 \
  -e POSTGRES_DB=jpd_db \
  -p 5432:5432 \
  -d postgres:17.5
```

This will create a PostgreSQL server running on `localhost:5432`.

---

### 3. Create Spring Boot Project

Generate a new Spring Boot project via [start.spring.io](https://start.spring.io/) with the following dependencies:

- Spring Web
- Spring Data JPA
- PostgreSQL Driver

Then extract or place it into this project folder.

---

### 4. Configure `application.properties`

Edit the file `src/main/resources/application.properties`:

```properties
spring.application.name=registration

# === Database Connection ===
spring.datasource.url=jdbc:postgresql://localhost:5432/jpd_db
spring.datasource.username=jpd
spring.datasource.password=123456

# === JPA ===
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

---

### 5. Create Database Schema via Entities

#### `School` Entity

```java
@Entity
@Table(name = "schools")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "school_seq")
    @SequenceGenerator(name = "school_seq", sequenceName = "school_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
```

#### `Student` Entity

```java
@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    @SequenceGenerator(name = "student_seq", sequenceName = "student_seq", allocationSize = 1)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
```

Hibernate will automatically create the two tables if they do not already exist.

---

## Database Result

Once the application starts, it will connect to PostgreSQL and automatically create the tables:

### Table: `schools`

| Column      | Type           | Constraints                  |
|-------------|----------------|------------------------------|
| id          | BIGSERIAL      | PK, NOT NULL                 |
| name        | VARCHAR        | NOT NULL, UNIQUE             |
| created_at  | TIMESTAMP      | NOT NULL                     |
| updated_at  | TIMESTAMP      | NOT NULL                     |

### Table: `students`

| Column      | Type           | Constraints                          |
|-------------|----------------|--------------------------------------|
| id          | BIGSERIAL      | PK, NOT NULL                         |
| first_name  | VARCHAR        | NOT NULL                             |
| last_name   | VARCHAR        | NOT NULL                             |
| school_id   | BIGINT         | NOT NULL, FK → `schools.id`          |
| created_at  | TIMESTAMP      | NOT NULL                             |
| updated_at  | TIMESTAMP      | NOT NULL                             |

---

## How to Run the App

You can run the application with:

```bash
./mvnw spring-boot:run
```

Or use **VS Code** to run the `RegistrationApplication.java` file.

---