# Table Tennis Tournament Microservice

This is a Java Spring Boot application that manages table tennis tournaments. It allows you to create tournaments, add players, manage matches, and retrieve tournament information.

## Features

- Create a new tournament
- Add players to a tournament
- Retrieve all tournaments
- Retrieve a specific tournament by ID
- Retrieve all game tables of a specific tournament
- Retrieve all matches of a specific tournament
- Update the state of a tournament

- Brackets (single elimination)
  - Generate brackets for a tournament
  - Retrieve a specific bracket by ID
  - Retrieve all matches of a specific bracket
  - Update the state of a bracket

## Prerequisites

- Java 11 or higher
- Maven
- PostgreSQL

## How to Build

1. Clone the repository:

```bash
git clone https://github.com/LuminiteTime/Tournament-Microservice.git
```

2. Navigate to the project directory:

```bash
cd Tournament-Microservice
```

3. Build the project using Maven:

```bash
mvn clean install
```

## How to Run

1. Configure your PostgreSQL database detailes according to the `src/main/resources/application.properties` file.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tournaments
spring.datasource.username=postgres
spring.datasource.password=postgres
```

2. Run the application:

```bash
mvn spring-boot:run
```

## API Documentation

The API documentation is available at `/swagger` endpoint when the application is running.

## License

This project is licensed under the MIT License.