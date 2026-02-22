# LoanStreet Backend

REST API for managing loan records, built with Spring Boot 3, PostgreSQL, and OpenAPI code generation.

## Tech Stack

- **Java 21** / **Spring Boot 3.4**
- **PostgreSQL 18** with **Flyway** migrations
- **OpenAPI Generator** — controllers and DTOs generated from `swagger.yaml`
- **Lombok** — reduces boilerplate
- **Docker Compose** — one-command local setup

## Getting Started

### Docker (recommended)

```bash
docker compose up --build
```

The API will be available at `http://localhost:8080`. PostgreSQL is exposed on port `5433`.

### Local Development

Requires Java 21, Maven 3.9+, and a running PostgreSQL instance.

```bash
# Start Postgres (or use docker compose up postgres)
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/loanstreet
export SPRING_DATASOURCE_USERNAME=loanstreet
export SPRING_DATASOURCE_PASSWORD=loanstreet

mvn spring-boot:run
```

## API Endpoints

| Method | Path              | Description            |
|--------|-------------------|------------------------|
| POST   | `/api/loans`      | Create a new loan      |
| GET    | `/api/loans/{id}` | Retrieve a loan by ID  |
| PUT    | `/api/loans/{id}` | Update an existing loan|

Full spec: [`swagger.yaml`](swagger.yaml)

## Database Migrations

Schema changes are managed by Flyway. Migration scripts live in:

```
src/main/resources/db/migration/
```

Naming convention: `V<version>__<description>.sql` (e.g. `V1__create_loans_table.sql`).

## Project Structure

```
src/main/java/com/loanstreet/backend/
├── controller/      # REST controllers
├── model/           # JPA entities
├── repository/      # Spring Data repositories
├── service/         # Business logic
└── dto/             # Generated from OpenAPI spec
```
