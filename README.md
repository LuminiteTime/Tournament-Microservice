# Tournament Microservice

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17-437291?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)

REST API for managing **table tennis tournaments**: players, matches, tournament lifecycle, and **single-elimination brackets**. Persistence uses **PostgreSQL** via Spring Data JPA. Interactive API docs are served with **springdoc-openapi** (Swagger UI).

## Prerequisites

- **JDK 17**
- **Maven 3.8+**
- **PostgreSQL** (local install or Docker)

Alternatively, run the stack with Docker Compose (PostgreSQL + application container); see [docker-compose.yaml](docker-compose.yaml).

## Configuration

Default datasource settings live in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tournaments
spring.datasource.username=postgres
spring.datasource.password=postgres
```

Adjust URLs and credentials for your environment before starting.

## Build

```bash
git clone https://github.com/LuminiteTime/Tournament-Microservice.git
cd Tournament-Microservice
mvn clean verify
```

## Run (Maven)

Ensure PostgreSQL is running and the database exists, then:

```bash
mvn spring-boot:run
```

The application listens on port **8080** by default.

## Run (Docker Compose)

From the repository root:

```bash
docker compose up -d --build
```

This starts PostgreSQL and the service with `spring.datasource.url` pointing at the `db` service.

## API documentation

With the app running, open **Swagger UI** at:

```text
http://localhost:8080/swagger
```

OpenAPI is backed by `src/main/resources/static/docs/swagger.yaml`.

## Features (summary)

- Create and list tournaments; fetch by ID; update tournament state  
- Register players and query tournament game tables and matches  
- Brackets: generate single-elimination brackets, fetch by ID, list matches, update bracket state  

## License

This project is licensed under the MIT License. See [LICENSE](LICENSE).
