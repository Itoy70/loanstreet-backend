# LoanStreet Backend

REST API for managing loan records, built with Spring Boot 3, PostgreSQL, Flyway migrations, and OpenAPI code generation. Deployed on Google Cloud Run with Cloud SQL.

**Live URL:** https://loanstreet-backend-493662086412.us-central1.run.app

## Tech Stack

- **Java 21** / **Spring Boot 3.4**
- **PostgreSQL 18** with **Flyway** migrations
- **OpenAPI Generator** — controllers and DTOs generated from `swagger.yaml`
- **Google Cloud Run** — serverless container deployment
- **Cloud SQL** — managed PostgreSQL with IAM authentication
- **Lombok** — reduces boilerplate
- **Docker Compose** — one-command local setup

## Quick Start

### Local Development (Docker)

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

Full OpenAPI spec: [`swagger.yaml`](swagger.yaml)

### Example Request

```bash
curl -X POST https://loanstreet-backend-493662086412.us-central1.run.app/api/loans \
  -H "Content-Type: application/json" \
  -d '{"amount":250000,"interestRate":0.065,"lengthInMonths":360,"monthlyPaymentAmount":1580.17}'
```

## Testing with the Client

A TypeScript client is included in the [`client/`](client/) directory:

```bash
cd client
npm install

# Test against Cloud Run
API_URL=https://loanstreet-backend-493662086412.us-central1.run.app npm run dev

# Test locally
npm run dev
```

## Database Migrations

Schema changes are managed by Flyway. Migration scripts live in:

```
src/main/resources/db/migration/
```

Naming convention: `V<version>__<description>.sql` (e.g. `V1__create_loans_table.sql`).

Migrations run automatically on application startup in both local and Cloud Run environments.

## Deployment

### Prerequisites

- [Google Cloud SDK](https://cloud.google.com/sdk) installed and authenticated
- GCP Project: `gen-lang-client-0220347810`
- Cloud SQL instance: `loanstreet-db` (PostgreSQL 18)

### Deploy to Cloud Run

```bash
gcloud run deploy loanstreet-backend \
  --project=gen-lang-client-0220347810 \
  --region=us-central1 \
  --source=.
```

### Cloud SQL Connection

The backend connects to Cloud SQL using the [Cloud SQL Auth Proxy](https://cloud.google.com/sql/docs/postgres/connect-run) via Unix socket:

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql:///loanstreet?cloudSqlInstance=gen-lang-client-0220347810:us-central1:loanstreet-db&socketFactory=com.google.cloud.sql.postgres.SocketFactory
```

Connection is authenticated via IAM — no public IP or SSL certificates required.

## Project Structure

```
src/main/java/com/loanstreet/backend/
├── controller/      # REST controllers
├── model/           # JPA entities
├── repository/      # Spring Data repositories
├── service/         # Business logic
├── dto/             # Generated from OpenAPI spec
└── api/             # Generated API interfaces

src/main/resources/
├── db/migration/    # Flyway migration scripts
├── application.yml  # Spring configuration
└── swagger.yaml     # OpenAPI specification

client/
├── src/
│   ├── LoanClient.ts    # TypeScript API client
│   └── index.ts         # Example usage
└── package.json
```
