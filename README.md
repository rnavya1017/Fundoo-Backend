# Fundoo Notes Backend

## Overview

Fundoo Notes is a Spring Boot REST API for a notes management
application. The backend provides user authentication, secure note
management, labels, reminders, attachments, search, filtering,
pagination, archive, trash, restore, and permanent deletion.

The application uses PostgreSQL for persistent data storage, Redis for
JWT token management, and embedded Apache Artemis for messaging support.

## Features

### Authentication and Authorization

-   User registration
-   User login
-   JWT-based authentication
-   Secure password handling
-   Logout with token invalidation
-   Forgot password
-   Reset password
-   Spring Security protected APIs

### Notes

-   Create notes
-   View all notes
-   View a note by ID
-   Update notes
-   Move notes to trash
-   Restore notes
-   Permanently delete notes
-   Pin and unpin notes
-   Archive and unarchive notes
-   Search notes
-   Filter notes by:
    -   Pinned status
    -   Archived status
    -   Trashed status
    -   Reminder
    -   Date
    -   Color
    -   Label
-   Pagination
-   Add labels to notes
-   Remove labels from notes

### Labels

-   Create labels
-   View labels
-   Update labels
-   Delete labels
-   Assign labels to notes
-   Remove labels from notes

### Reminders

-   Create reminders for notes
-   View reminders
-   Delete reminders

### Attachments

-   Upload attachments to notes
-   View note attachments
-   Delete attachments

## Technology Stack

### Backend

-   Java 21
-   Spring Boot 4.1.1
-   Spring Web MVC
-   Spring Data JPA
-   Hibernate
-   Spring Security
-   Spring Validation
-   Spring AOP
-   Spring Data Redis
-   Apache Artemis
-   JWT
-   PostgreSQL
-   Maven
-   Lombok
-   Springdoc OpenAPI

## Architecture

The project follows a layered Spring Boot architecture.

``` text
Controller
    |
    v
Service
    |
    v
Repository
    |
    v
Database
```

The main layers are:

``` text
config
controller
dto
entity
exception
repository
redis
security
service
aspect
```

### Controller Layer

Handles HTTP requests and responses.

Main controllers:

-   AuthController
-   NoteController
-   LabelController
-   ReminderController
-   AttachmentController

### Service Layer

Contains application business logic.

Main services:

-   AuthService
-   NoteService
-   LabelService
-   ReminderService
-   AttachmentService

### Repository Layer

Uses Spring Data JPA repositories for database operations.

### DTO Layer

DTOs are used to transfer request and response data between the client
and server.

### Entity Layer

Contains JPA entities representing database tables.

Main entities:

-   User
-   Note
-   Label
-   Reminder
-   Attachment

### Security Layer

JWT authentication is implemented using Spring Security.

Main security classes:

-   SecurityConfig
-   JwtAuthenticationFilter
-   JwtService

### Redis

Redis is used by the token cache service for JWT token management and
logout invalidation.

### AOP

Logging is implemented using Spring AOP.

Main class:

-   LoggingAspect

## Database

The application uses PostgreSQL.

Default database configuration:

``` properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fundoo_notes_db
spring.datasource.username=postgres
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

Create the database before starting the application:

``` sql
CREATE DATABASE fundoo_notes_db;
```

The application uses:

``` properties
spring.jpa.hibernate.ddl-auto=update
```

so Hibernate can update the database schema based on the entities.

## Redis

Redis is configured on the default local port:

``` properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

Make sure Redis is running before starting the backend.

## Apache Artemis

The application is configured to use embedded Artemis:

``` properties
spring.artemis.mode=embedded
```

## Application Configuration

The backend runs on:

``` text
http://localhost:8080
```

Important configuration from `application.properties`:

``` properties
server.port=8080

spring.application.name=FundooNotes

spring.datasource.url=jdbc:postgresql://localhost:5432/fundoo_notes_db
spring.datasource.username=postgres
spring.datasource.password=root

jwt.expiration=3600000

spring.data.redis.host=localhost
spring.data.redis.port=6379

spring.artemis.mode=embedded

spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=6MB
```

For a real deployment, replace development credentials and secrets with
environment-specific secure values.

## REST API

Base URL:

``` text
http://localhost:8080
```

### Authentication

  Method   Endpoint                      Description
  -------- ----------------------------- -------------------------------
  POST     `/api/auth/register`          Register a new user
  POST     `/api/auth/login`             Login
  POST     `/api/auth/logout`            Logout
  POST     `/api/auth/forgot-password`   Generate password reset token
  POST     `/api/auth/reset-password`    Reset password

### Notes

  --------------------------------------------------------------------------------------------
  Method                  Endpoint                                     Description
  ----------------------- -------------------------------------------- -----------------------
  POST                    `/api/notes`                                 Create a note

  GET                     `/api/notes`                                 Get all notes

  GET                     `/api/notes/{id}`                            Get note by ID

  PUT                     `/api/notes/{id}`                            Update a note

  DELETE                  `/api/notes/{id}`                            Move note to trash

  GET                     `/api/notes/trash`                           Get trashed notes

  PATCH                   `/api/notes/{id}/pin`                        Pin a note

  PATCH                   `/api/notes/{id}/unpin`                      Unpin a note

  PATCH                   `/api/notes/{id}/archive`                    Archive a note

  PATCH                   `/api/notes/{id}/unarchive`                  Unarchive a note

  PATCH                   `/api/notes/{id}/restore`                    Restore a note

  DELETE                  `/api/notes/{id}/permanent`                  Permanently delete a
                                                                       note

  GET                     `/api/notes/search?keyword=...`              Search notes

  GET                     `/api/notes/filter/pinned?pinned=true`       Filter by pinned status

  GET                     `/api/notes/filter/archived?archived=true`   Filter by archived
                                                                       status

  GET                     `/api/notes/filter/trashed?trashed=true`     Filter by trash status

  GET                     `/api/notes/filter/reminder`                 Get notes with
                                                                       reminders

  GET                     `/api/notes/filter/date?date=YYYY-MM-DD`     Filter by date

  GET                     `/api/notes/filter/color?color=...`          Filter by color

  GET                     `/api/notes/filter/label?label=...`          Filter by label

  GET                     `/api/notes/page?page=0&size=5`              Get paginated notes

  POST                    `/api/notes/{id}/labels?labelId=...`         Add label to a note

  DELETE                  `/api/notes/{id}/labels/{labelId}`           Remove label from a
                                                                       note
  --------------------------------------------------------------------------------------------

### Labels

  Method   Endpoint             Description
  -------- -------------------- ----------------
  POST     `/api/labels`        Create a label
  GET      `/api/labels`        Get all labels
  PUT      `/api/labels/{id}`   Update a label
  DELETE   `/api/labels/{id}`   Delete a label

### Reminders

  Method   Endpoint                     Description
  -------- ---------------------------- -------------------
  POST     `/api/notes/{id}/reminder`   Create a reminder
  GET      `/api/reminders`             Get all reminders
  DELETE   `/api/reminders/{id}`        Delete a reminder

### Attachments

  Method   Endpoint                        Description
  -------- ------------------------------- ----------------------
  POST     `/api/notes/{id}/attachments`   Upload an attachment
  GET      `/api/notes/{id}/attachments`   Get note attachments
  DELETE   `/api/attachments/{id}`         Delete an attachment

## Authentication Flow

The authentication flow is:

``` text
Client
  |
  | Register/Login
  v
AuthController
  |
  v
AuthService
  |
  v
User Repository
  |
  v
PostgreSQL

Login
  |
  v
JWT Token
  |
  v
Client
  |
  | Authorization: Bearer <JWT>
  v
JwtAuthenticationFilter
  |
  v
Protected Controller
```

The frontend sends the JWT token using the Authorization header:

``` text
Authorization: Bearer <JWT>
```

## Running the Application

### Prerequisites

Install and configure:

-   Java 21
-   Maven
-   PostgreSQL
-   Redis

### 1. Start PostgreSQL

Make sure PostgreSQL is running.

Create the database:

``` sql
CREATE DATABASE fundoo_notes_db;
```

### 2. Start Redis

Make sure Redis is running on:

``` text
localhost:6379
```

### 3. Open the project

Go to the directory containing `pom.xml`.

### 4. Install dependencies and run

Using Maven:

``` bash
mvn spring-boot:run
```

On Windows, Maven Wrapper can be used:

``` bat
mvnw.cmd spring-boot:run
```

### 5. Verify the backend

The backend should start on:

``` text
http://localhost:8080
```

## Build the Application

To compile and package the project:

``` bash
mvn clean package
```

To skip tests:

``` bash
mvn clean package -DskipTests
```

## API Documentation

Springdoc OpenAPI is included in the project.

After starting the application, Swagger UI is available at:

``` text
http://localhost:8080/swagger-ui/index.html
```

The OpenAPI specification is available at:

``` text
http://localhost:8080/v3/api-docs
```

## File Upload Limits

The application is configured with:

``` properties
spring.servlet.multipart.max-file-size=5MB
spring.servlet.multipart.max-request-size=6MB
```

Individual uploaded files can be up to 5 MB.

## Logging

Application logging is configured with:

``` properties
logging.level.com.bridgelabz.fundoo.notes=INFO
logging.file.name=logs/application.log
```

Logs are written to:

``` text
logs/application.log
```

## Project Structure

``` text
FundooNotes/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/bridgelabz/fundoo/notes/
│   │   │       ├── aspect/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── exception/
│   │   │       ├── redis/
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       └── service/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── INTEGRATION-README.md
```

## Error Handling

The application uses a global exception handler:

``` text
GlobalExceptionHandler
```

It provides centralized handling for application exceptions and returns
structured error responses.

Examples of custom exceptions include:

-   UserNotFoundException
-   NoteNotFoundException
-   LabelNotFoundException
-   ReminderNotFoundException
-   AttachmentNotFoundException
-   DuplicateEmailException
-   InvalidPasswordException
-   InvalidTokenException

## Frontend Integration

The backend is designed to work with an Angular frontend running on:

``` text
http://localhost:4200
```

The integration configuration allows the Angular application to
communicate with the Spring Boot REST API.

The frontend uses JWT authentication for protected requests.

## Development Notes

-   PostgreSQL is the primary persistent database.
-   Redis is used for token caching and token invalidation.
-   JWT is used for stateless authentication.
-   JPA/Hibernate handles database persistence.
-   Validation is applied to request DTOs.
-   AOP is used for logging.
-   Apache Artemis is configured for messaging support.
-   Swagger/OpenAPI is included for API documentation.
-   Multipart upload support is enabled for note attachments.

## License

This project does not define a specific open-source license in the
current Maven project configuration.
