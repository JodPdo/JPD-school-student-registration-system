# JPD School and Student Registration System

This project is backend development series using Spring Boot and PostgreSQL.
It sets up a basic registration system with two untities: 'School' and 'Student'.

## Getting Started

Follow the instructions below to run this project on your local machine.

---

## Prerequisites

- Java 17+
- Maven
- Docker (for running PostgreSQL)
- Git

---

## Project Setup

### 1. Clone the Repository

'''bash
git clone http://github.com/JpdPdo/JPD-school-school-student-registration-system.git
cd JPD-school-student-registration-system
git checkout -b feat/1-project-setup

---

### 2. Start PostgreSQL using Docker

First, pull the official PostgreSQL image:

'''bash
docker pull postgres:17.5
'''

Then, run a new contrainer:

'''bash
docker run --name jpd-postgres \
    -e POSTGRES_USER=jpd \
    -e POSTGRES_PASSWORD=123456 \
    -e POSTGRES_DB=jpd_db \
    -p 5432:5432 \
    -d postgres:17.5
'''

this will create a PostgreSQL server running on 'localhost:5432'.

---

### 3. Create Spring Boot Project

you can generate a new Spring Boot project via [start.spring.io](https://start.spring.io/) with the following dependencies:

- Spring Web
- Spring Data JPA
- PostgreSQL Driver

Then extract or clone it into this project folder.

---

### 4. Configure `application.properties`

Edit the file `src/main/resources/application.properties`;

```properties
spring.application.name=registration

# === DataBase Connection ===
spring.datasource.url=jdbc:postgresql://localhost:5432/jpd_db
spring.datasource.username=jpd
spring.datasource.password=123456

# --- JPA ---
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

---

### 5. Create DataBase Schema via Entities

### `School` Entity

```java
@Entity
@Table(name = "school")
public class School
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;
}
```

### `Student` Entity

```java
@Entity
@Table(name = "student")
public class Student
{
    @Id
    @GeneratedValue(strategy = Generation.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToOne
    @JoinCulumn(name = "school_id", nullable = false)
    private School school;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;
}
```

Hibernate will automatically create the two tables only if they don’t already exist.

---

## Result

Once the application starts, it will connect to PostgreSQL and automatically create the tables:

### Table: `schools`

| Column      | Type      | Constraints                  |
|-------------|-----------|------------------------------|
| id          | SERIAL    | PK, NOT NULL                 |
| name        | VARCHAR   | NOT NULL, UNIQUE             |
| created_at  | DATE      | NOT NULL                     |
| updated_at  | DATE      | NOT NULL                     |

### Table: `students`

| Column      | Type      | Constraints                  |
|-------------|-----------|------------------------------|
| id          | SERIAL    | PK, NOT NULL                 |
| first_name  | VARCHAR   | NOT NULL                     |
| last_name   | VARCHAR   | NOT NULL                     |
| school_id   | INTEGER   | NOT NULL, FK to `schools.id` |
| created_at  | DATE      | NOT NULL                     |
| updated_at  | DATE      | NOT NULL                     |

---

## How to Test

Run the Spring Boot App:

```
./mvnw spring-boot:run
```

--- Or you can run it through VS Code (check the RegistrationApplication.java file and run it).

###