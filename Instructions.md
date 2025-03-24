# Instructions

## Table of Contents
1. [Project Overview](#project-overview)
2. [Requirements](#requirements)
3. [Getting the Code](#getting-the-code)
4. [Local Setup](#local-setup)
   - [1. Start the Database (PostgreSQL) with Docker](#1-start-the-database-postgresql-with-docker)
   - [2. Build and Run the Spring Boot App](#2-build-and-run-the-spring-boot-app)
5. [Project Structure](#project-structure)
6. [Testing](#testing)
7. [API Endpoints](#api-endpoints)
   - [Movies](#movies)
   - [Showtimes](#showtimes)
   - [Bookings](#bookings)
8. [Error Handling](#error-handling)
9. [Notes and Common Issues](#notes-and-common-issues)

---

## Project Overview
This project is a **Spring Boot** application designed for a movie ticket booking system, referred to as **“Popcorn Palace.”** It provides RESTful endpoints for:
- Managing movies,
- Managing showtimes (with no overlapping schedules per theater),
- Booking tickets for specific showtimes (ensuring no seat duplication).

The **TDP popcorn-palace requirements document** specifies the core features:
- **Movie Management** (Create/Read/Update/Delete),
- **Showtime Management** (Create/Read/Update/Delete),
- **Ticket Bookings** (Create only),
- **Validation and Error Handling** for invalid or duplicate data,
- Basic tests.

---

## Requirements
1. **Java**:
   - Java 21 (as per the `pom.xml` property `<java.version>21</java.version>`).

2. **Maven**:
   - Maven 3.6+ (or use the provided `mvnw`/`mvnw.cmd` wrapper scripts).

3. **Docker & Docker Compose**:
   - Needed if you plan to run PostgreSQL using the provided `compose.yml`.
   - If you prefer your own database setup, you can point the application to any running PostgreSQL instance. Adjust `application.yaml` accordingly.

4. **PostgreSQL**:
   - The application expects a PostgreSQL database with:
      - username: `popcorn-palace`
      - password: `popcorn-palace`
      - database: `popcorn-palace`
   - These defaults are configured in `application.yaml`.

5. **Port Availability**:
   - The application defaults to **port 8080** (defined in `application.yaml`).

---

## Getting the Code
1. **Clone** the repository:
   ```bash
   git clone https://github.com/Maorazr/popcorn-palace.git
   ```
2. **Navigate** to the project root:
   ```bash
   cd popcorn-palace
---

## Local Setup

### 1. Start the Database (PostgreSQL) with Docker
1. Make sure you have Docker and Docker Compose installed.
2. In your project root directory (where `compose.yml` resides), run:
   ```bash
   docker compose up -d
   ```
   This will pull and start a **Postgres** container listening on port **5432** with the credentials specified in `compose.yml`.

3. Verify the container is running:
   ```bash
   docker ps
   ```
   You should see a container named something like `popcorn_palace-db-1` or similar.

### 2. Build and Run the Spring Boot App

#### (A) Using Maven Wrapper
1. **Build** and run tests (optional but recommended):
   ```bash
   ./mvnw clean install
   ```
   This compiles the code and runs all tests. If you just want to check the project compiles without running tests, use `-DskipTests`.

2. **Run** the Spring Boot app:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Once started, the app will be available at [http://localhost:8080](http://localhost:8080).

#### (B) Using Direct Maven Commands
If you have Maven installed locally:
1. **Build** and run tests:
   ```bash
   mvn clean install
   ```
2. **Run** the application:
   ```bash
   mvn spring-boot:run
   ```

#### (C) Running the Packaged JAR
If you prefer to build a JAR and then run it:
1. Generate the JAR:
   ```bash
   mvn clean package
   ```
2. Run the generated artifact:
   ```bash
   java -jar target/popcorn-palace-0.0.1-SNAPSHOT.jar
   ```
   The service will start on port **8080** by default.

---

## Project Structure

```
.
├── DeveloperNotes.md       <-- Developer notes and implementation insights 
├── Instructions.md         <-- (This file)
├── compose.yml             <-- Docker Compose file for PostgreSQL
├── mvnw / mvnw.cmd         <-- Maven wrapper scripts
├── pom.xml                 <-- Maven build configuration
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.att.tdp.popcorn_palace
│   │   │       ├── PopcornPalaceApplication.java  <-- Main Spring Boot entry
│   │   │       ├── controller                     <-- REST Controllers
│   │   │       ├── dto                            <-- Data Transfer Objects
│   │   │       ├── entity                         <-- JPA Entities
│   │   │       ├── exception                      <-- Custom exceptions & handler
│   │   │       ├── repository                     <-- Spring Data JPA repositories
│   │   │       ├── service                        <-- Business logic services
│   │   │       └── util                           <-- Utility class
│   │   └── resources
│   │       ├── application.yaml                   <-- Dev environment config
│   │       ├── data.sql                           <-- Sample seed data
│   │       └── schema.sql                         <-- DB schema creation
│   └── test
│       ├── java
│       │   └── com.att.tdp.popcorn_palace
│       │       ├── PopcornPalaceApplicationTests.java
│       │       ├── controller                     <-- Controller tests
│       │       └── service                        <-- Service tests
│       └── resources
│           └── application.yaml                   <-- Test config
└── untrackedFiles
    └── Readme.md
```

Key points:
- **`application.yaml`** in `src/main/resources` is the main configuration for dev (points to PostgreSQL).
- **`data.sql`** and **`schema.sql`** handle initial DB setup and seed data for dev environment.
- Tests can be found under `src/test/java`.

---

## Testing
To **run all tests**, simply execute:
```bash
./mvnw clean test
```
(or `mvn clean test` if you have Maven installed).

If you’ve already done `mvn clean install`, your tests have also been run.

---

## API Endpoints

Below is a **high-level** description of the main endpoints. For more details, see the `Readme.md` or explore the code under `controller/`:

### Movies
**Base URL**: `/movies`

1. **GET** `/movies/all`
   - Fetch all available movies.
   - **200 OK** on success.

2. **POST** `/movies`
   - Create a new movie.
   - Expects a JSON body with `title`, `genre`, `duration`, `rating`, and `releaseYear`.
   - **200 OK** + created movie object on success.
   - **409 CONFLICT** if a movie with the same title exists.

3. **POST** `/movies/update/{movieTitle}`
   - Update an existing movie with the given title.
   - **200 OK** on success.
   - **404 NOT FOUND** if the movie does not exist.

4. **DELETE** `/movies/{movieTitle}`
   - Delete a movie by title.
   - **200 OK** on success.
   - **404 NOT FOUND** if not found.

### Showtimes
**Base URL**: `/showtimes`

1. **GET** `/showtimes/{showtimeId}`
   - Fetch a showtime by its ID.
   - **200 OK** on success.
   - **404 NOT FOUND** if not found.

2. **POST** `/showtimes`
   - Create a new showtime.
   - Expects `movieId`, `theater`, `startTime`, `endTime`, `price`.
   - **200 OK** + created showtime data on success.
   - **409 CONFLICT** if there’s an overlapping showtime for the same theater.
   - **404 NOT FOUND** if the associated `movieId` doesn’t exist.

3. **POST** `/showtimes/update/{showtimeId}`
   - Update an existing showtime by ID.
   - **200 OK** on success.
   - **404 NOT FOUND** if not found.

4. **DELETE** `/showtimes/{showtimeId}`
   - Delete a showtime by its ID.
   - **200 OK** on success.
   - **404 NOT FOUND** if not found.

### Bookings
**Base URL**: `/bookings`

1. **POST** `/bookings`
   - Create a new booking.
   - Expects a JSON body with `showtimeId`, `seatNumber`, `userId`.
   - **200 OK** + returns JSON with `"id": <UUID>` on success.
   - **404 NOT FOUND** if the showtime does not exist.
   - **409 CONFLICT** if the seat is already booked for that showtime.

---

## Error Handling
- Validation errors (e.g., invalid input or blank fields) return **400 BAD REQUEST** with a JSON body describing the error fields.
- Not found errors (`MovieNotFoundException`, `ShowtimeNotFoundException`, etc.) return **404 NOT FOUND**.
- Conflicts (e.g., seat or showtime overlaps) return **409 CONFLICT**.
- A custom `GlobalExceptionHandler` centralizes these responses.

---

## Notes and Common Issues
1. **Database Connection**: If you see errors connecting to the DB, ensure Docker is running and you started the `db` service via `docker compose up`.
2. **Port Conflicts**: If port **8080** is in use, change it in `application.yaml` or use `server.port` property at runtime (e.g. `-Dserver.port=9090`).
3. **Data Initialization**: The included `data.sql` seeds sample movies and showtimes. If you need a clean slate, remove or modify the `data.sql` calls.
4. **Java Version**: Make sure to use Java **21** or update the `pom.xml` to your installed version.

---