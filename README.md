# Fundoo Notes – Backend

A secure and scalable **Fundoo Notes REST API** built using **Java 21 and Spring Boot**. The application provides user authentication, note management, labels, reminders, attachments, search, filtering, pagination, Redis-based token management, and asynchronous reminder notifications using JMS and ActiveMQ Artemis.

---

## 🚀 Features

* User registration and login
* JWT-based authentication
* BCrypt password encryption
* Stateless Spring Security
* Redis-based JWT token validation and logout
* Create, read, update and delete notes
* Move notes to trash and permanently delete
* Pin and unpin notes
* Archive and unarchive notes
* Restore notes from trash
* Search notes by keyword
* Filter notes by:

  * Pinned status
  * Archived status
  * Trash status
  * Reminder
  * Date
  * Label
* Pagination for notes
* Create, update and delete labels
* Attach files to notes
* Reminder management
* JMS-based reminder notifications
* ActiveMQ Artemis embedded message broker
* Dead Letter Queue (DLQ) configuration
* Centralized exception handling
* Input validation
* AOP-based logging
* PostgreSQL database integration
* CORS support for Angular frontend

---

## 🛠️ Technologies Used

| Technology        | Purpose                        |
| ----------------- | ------------------------------ |
| Java 21           | Programming Language           |
| Spring Boot 4.1.1 | Backend Framework              |
| Spring Web MVC    | REST APIs                      |
| Spring Data JPA   | Database Operations            |
| Spring Security   | Authentication & Authorization |
| JWT               | Token-Based Authentication     |
| BCrypt            | Password Encryption            |
| PostgreSQL        | Relational Database            |
| Redis             | JWT Token Cache                |
| JMS               | Messaging                      |
| ActiveMQ Artemis  | Message Broker                 |
| Spring AOP        | Logging                        |
| Bean Validation   | Request Validation             |
| Maven             | Build Tool                     |
| Lombok            | Boilerplate Reduction          |

---

## 🏗️ Project Architecture

The backend follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

Additional infrastructure:

```text
Client / Angular
       ↓
Spring Security
       ↓
JWT Authentication Filter
       ↓
Controller
       ↓
Service
       ↓
Repository
       ↓
PostgreSQL

JWT Token
    ↓
Redis Token Cache

Reminder
    ↓
JMS Producer
    ↓
ActiveMQ Artemis
    ↓
JMS Consumer
    ↓
Notification Processing
```

---

## 📁 Project Structure

```text
src/main/java/com/bridgelabz/fundoo/notes/

├── aspect/
│   └── LoggingAspect.java
│
├── config/
│   ├── ArtemisConfig.java
│   ├── JmsConfig.java
│   └── RedisConfig.java
│
├── controller/
│   ├── AuthController.java
│   ├── NoteController.java
│   ├── LabelController.java
│   ├── ReminderController.java
│   └── AttachmentController.java
│
├── dto/
│   ├── AuthResponseDTO.java
│   ├── LoginRequestDTO.java
│   ├── RegisterRequestDTO.java
│   ├── NoteRequestDTO.java
│   ├── NoteResponseDTO.java
│   ├── LabelRequestDTO.java
│   ├── LabelResponseDTO.java
│   ├── ReminderRequestDTO.java
│   ├── ReminderResponseDTO.java
│   └── ...
│
├── entity/
│   ├── User.java
│   ├── Note.java
│   ├── Label.java
│   ├── Reminder.java
│   └── Attachment.java
│
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ErrorResponse.java
│   ├── UserNotFoundException.java
│   ├── NoteNotFoundException.java
│   ├── LabelNotFoundException.java
│   ├── ReminderNotFoundException.java
│   └── ...
│
├── jms/
│   ├── NotificationProducer.java
│   └── NotificationConsumer.java
│
├── redis/
│   └── TokenCacheService.java
│
├── repository/
│   ├── UserRepository.java
│   ├── NoteRepository.java
│   ├── LabelRepository.java
│   ├── ReminderRepository.java
│   └── AttachmentRepository.java
│
├── security/
│   ├── JwtService.java
│   ├── JwtAuthenticationFilter.java
│   └── SecurityConfig.java
│
└── service/
    ├── AuthService.java
    ├── NoteService.java
    ├── LabelService.java
    ├── ReminderService.java
    ├── AttachmentService.java
    │
    └── impl/
        ├── AuthServiceImpl.java
        ├── NoteServiceImpl.java
        ├── LabelServiceImpl.java
        ├── ReminderServiceImpl.java
        └── AttachmentServiceImpl.java
```

---

## 🔐 Authentication Flow

The application uses **JWT-based stateless authentication**.

### Registration

```text
Client
  ↓
POST /api/auth/register
  ↓
Validate Request
  ↓
Encrypt Password using BCrypt
  ↓
Save User in PostgreSQL
```

### Login

```text
Client
  ↓
POST /api/auth/login
  ↓
Validate Email & Password
  ↓
Generate JWT
  ↓
Store Token in Redis
  ↓
Return JWT to Client
```

### Access Protected API

```text
Client
  ↓
Authorization: Bearer <JWT>
  ↓
JwtAuthenticationFilter
  ↓
Check Token in Redis
  ↓
Validate JWT
  ↓
Authenticate User
  ↓
Controller
```

JWT tokens are configured with a **1-hour expiration period**.

---

## 📝 Notes Management

Users can:

* Create notes
* View notes
* Update notes
* Move notes to trash
* Permanently delete notes
* Restore notes
* Pin/unpin notes
* Archive/unarchive notes
* Search notes
* Filter notes
* Add/remove labels
* Add reminders

Deleting a note normally moves it to the **trash** instead of immediately removing it from the database.

Permanent deletion is performed using:

```text
DELETE /api/notes/{id}/permanent
```

---

## 🏷️ Labels

Each user can create and manage their own labels.

Supported operations:

```text
POST   /api/labels
GET    /api/labels
PUT    /api/labels/{id}
DELETE /api/labels/{id}
```

Notes and labels have a **Many-to-Many relationship**.

```text
Note ←→ Label
```

---

## ⏰ Reminders & JMS

The backend uses **JMS with ActiveMQ Artemis** for reminder notification processing.

When a reminder is created:

```text
Create Reminder
      ↓
Save Reminder
      ↓
Create JMS Message
      ↓
ActiveMQ Artemis Queue
      ↓
Notification Consumer
      ↓
Process Notification
```

Queue:

```text
fundoo.reminder.queue
```

The application also configures:

* Maximum delivery attempts: `3`
* Redelivery delay: `2000 ms`
* Dead Letter Queue: `fundoo.reminder.dlq`

---

## ⚡ Redis Token Management

Redis is used to store active JWT tokens.

```text
Login
  ↓
Generate JWT
  ↓
Redis
  ↓
auth:token:<token>
```

During every authenticated request:

```text
JWT
 ↓
Check Redis
 ↓
Token exists?
 ↓
Validate JWT
 ↓
Allow Request
```

Logout removes the token from Redis, making the token invalid before its normal expiration.

---

## 📎 File Attachments

Users can upload attachments to their notes.

Supported file types include:

* PDF
* JPG
* JPEG
* PNG
* DOC
* DOCX

Maximum file size:

```text
5 MB
```

Uploaded files are stored in:

```text
uploads/
```

Attachment metadata such as filename, type, size, path and upload date is stored in PostgreSQL.

---

## 🗄️ Database

The application uses **PostgreSQL**.

Main entities:

```text
User
 │
 ├── Notes
 │     ├── Labels
 │     ├── Reminders
 │     └── Attachments
 │
 └── Authentication Information
```

Entity relationships:

```text
User 1 ──────── * Note

User 1 ──────── * Label

Note * ──────── * Label

Note 1 ──────── * Reminder

Note 1 ──────── * Attachment
```

Hibernate automatically manages the database schema using:

```properties
spring.jpa.hibernate.ddl-auto=update
```

---

## 🔎 Search, Filtering & Pagination

### Search

```text
GET /api/notes/search?keyword=java
```

### Filter by Pin

```text
GET /api/notes/filter/pinned?pinned=true
```

### Filter by Archive

```text
GET /api/notes/filter/archived?archived=true
```

### Filter by Trash

```text
GET /api/notes/filter/trashed?trashed=true
```

### Filter by Color

```text
GET /api/notes/filter/color?color=yellow
```

### Filter by Label

```text
GET /api/notes/filter/label?label=work
```

### Filter by Date

```text
GET /api/notes/filter/date?date=2026-09-26
```

### Pagination

```text
GET /api/notes/page?page=0&size=5
```

---

## 🌐 REST API Endpoints

### Authentication

| Method | Endpoint                    | Description          |
| ------ | --------------------------- | -------------------- |
| POST   | `/api/auth/register`        | Register user        |
| POST   | `/api/auth/login`           | Login                |
| POST   | `/api/auth/logout`          | Logout               |
| POST   | `/api/auth/forgot-password` | Generate reset token |
| POST   | `/api/auth/reset-password`  | Reset password       |

### Notes

| Method | Endpoint                    | Description        |
| ------ | --------------------------- | ------------------ |
| POST   | `/api/notes`                | Create note        |
| GET    | `/api/notes`                | Get all notes      |
| GET    | `/api/notes/{id}`           | Get note           |
| PUT    | `/api/notes/{id}`           | Update note        |
| DELETE | `/api/notes/{id}`           | Move to trash      |
| GET    | `/api/notes/trash`          | Get trashed notes  |
| PATCH  | `/api/notes/{id}/pin`       | Pin note           |
| PATCH  | `/api/notes/{id}/unpin`     | Unpin note         |
| PATCH  | `/api/notes/{id}/archive`   | Archive note       |
| PATCH  | `/api/notes/{id}/unarchive` | Unarchive note     |
| PATCH  | `/api/notes/{id}/restore`   | Restore note       |
| DELETE | `/api/notes/{id}/permanent` | Permanently delete |
| GET    | `/api/notes/search`         | Search notes       |
| GET    | `/api/notes/page`           | Paginated notes    |

### Labels

| Method | Endpoint           | Description  |
| ------ | ------------------ | ------------ |
| POST   | `/api/labels`      | Create label |
| GET    | `/api/labels`      | Get labels   |
| PUT    | `/api/labels/{id}` | Update label |
| DELETE | `/api/labels/{id}` | Delete label |

### Reminders

| Method | Endpoint                   | Description     |
| ------ | -------------------------- | --------------- |
| POST   | `/api/notes/{id}/reminder` | Create reminder |
| GET    | `/api/reminders`           | Get reminders   |
| DELETE | `/api/reminders/{id}`      | Delete reminder |

### Attachments

| Method | Endpoint                      | Description       |
| ------ | ----------------------------- | ----------------- |
| POST   | `/api/notes/{id}/attachments` | Upload attachment |
| GET    | `/api/notes/{id}/attachments` | Get attachments   |
| DELETE | `/api/attachments/{id}`       | Delete attachment |

---

---

## ▶️ Run the Application

Clone the repository:

```bash
git clone <YOUR_BACKEND_REPOSITORY_URL>
```

Navigate to the project:

```bash
cd FundooNotes
```

Run using Maven:

```bash
mvn spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The backend will start at:

```text
http://localhost:8080
```

---


## 🌐 Frontend Integration

The backend is configured to communicate with an Angular frontend running on:

```text
http://localhost:4200
```

CORS is configured to allow:

```text
http://localhost:4200
```

The frontend sends JWT authentication using:

```text
Authorization: Bearer <JWT_TOKEN>
```

---

## 🧪 Testing

Run the test suite using:

```bash
mvn test
```

The project contains Spring Boot test configuration under:

```text
src/test/
```

---

## 📊 Logging

The application uses logging for monitoring backend operations.

Log file:

```text
logs/application.log
```

AOP logging is implemented using:

```text
LoggingAspect.java
```

This helps track service-layer operations and application behavior.

---

## 🛡️ Exception Handling

The application uses centralized exception handling through:

```text
GlobalExceptionHandler
```

Custom exceptions include:

```text
UserNotFoundException
NoteNotFoundException
LabelNotFoundException
ReminderNotFoundException
AttachmentNotFoundException
DuplicateEmailException
InvalidPasswordException
InvalidTokenException
```

This provides consistent error responses to API clients.

---

## 🔄 Complete Application Flow

```text
Angular Frontend
       │
       ▼
REST Controller
       │
       ▼
JWT Authentication Filter
       │
       ├──────────► Redis Token Validation
       │
       ▼
Service Layer
       │
       ├──────────► PostgreSQL
       │
       ├──────────► Redis
       │
       └──────────► JMS Producer
                         │
                         ▼
                  ActiveMQ Artemis
                         │
                         ▼
                  JMS Consumer
                         │
                         ▼
                    Notification
```

---

## 👩‍💻 Author

**R Navya**

Java Backend / Full Stack Developer

### Technologies

```text
Java | Spring Boot | Spring Security | JWT
PostgreSQL | JPA | Redis | JMS
ActiveMQ Artemis | REST API | Maven
```

---

## 📌 Project Highlights

This project demonstrates practical implementation of:

* RESTful API development
* Layered backend architecture
* Spring Security and JWT authentication
* Password encryption using BCrypt
* Redis caching
* PostgreSQL database management
* JPA entity relationships
* File upload handling
* Search and filtering
* Pagination
* JMS messaging
* ActiveMQ Artemis
* Dead Letter Queue
* AOP logging
* Global exception handling
* DTO-based API design
* Angular and Spring Boot integration
