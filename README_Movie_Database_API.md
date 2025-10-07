# 🎬 Movie Database API

## 📖 Project Overview

The **Movie Database API** is a RESTful application built with **Spring Boot** and **Spring Data JPA**, designed to help the local film society digitize and manage their movie collection.  
It provides endpoints to manage **movies**, **genres**, and **actors**, allowing easy creation, update, search, and deletion of entries while handling complex relationships between them.

### 🧩 Key Features
- CRUD operations for **Movies**, **Genres**, and **Actors**
- Many-to-Many relationships:
  - Movies ↔ Genres
  - Movies ↔ Actors
- Filtering and searching:
  - Movies by genre, year, or actor
  - Actors by name (case-insensitive)
- Pagination for large datasets
- Partial updates using **PATCH**
- Force deletion with relationship cleanup
- Input validation and custom error handling
- Integrated testing via **Postman**, **Curl**, and **Swagger UI**
- SQLite database (simple and file-based)

---

## ⚙️ Setup and Installation Instructions

### 1. Prerequisites
Before you begin, ensure you have installed:
- **Java 17** or newer  
- **Maven 3.8+**
- **SQLite** (no setup required beyond installation)

### 2. Clone and Build
```bash
git clone <your-repo-url>
cd kmdb
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```
Once started, the API will be available at:
```
http://localhost:8080
```

### 4. Database Configuration
The application uses SQLite by default. Configuration is in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:sqlite:movies.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
```
This automatically creates a file `movies.db` in your project root on first run.

---

## 🚀 Usage Guide

### 1. Entities Overview
| Entity | Fields | Example |
|--------|---------|----------|
| **Genre** | `id`, `name` | Action |
| **Movie** | `id`, `title`, `releaseYear`, `duration` | Inception (2010, 148) |
| **Actor** | `id`, `name`, `birthDate` | Leonardo DiCaprio (1974-11-11) |

Relationships:
- A movie can belong to multiple genres and feature multiple actors.
- An actor can star in many movies.

---

### 2. API Endpoints Summary

| Method | Endpoint | Description |
|--------|-----------|-------------|
| **POST** | `/api/movies` | Create a new movie |
| **GET** | `/api/movies` | List all movies (supports `?genre=`, `?year=`, `?actor=`, `?page=`, `?size=`) |
| **GET** | `/api/movies/{id}` | Get movie by ID |
| **PATCH** | `/api/movies/{id}` | Update movie fields (partial) |
| **DELETE** | `/api/movies/{id}?force=true` | Delete movie, optionally force-remove relationships |
| **GET** | `/api/movies/{movieId}/actors` | List all actors in a movie |
| **GET** | `/api/movies/search?title=` | Search movies by partial title |
| **GET** | `/api/genres` | Retrieve all genres |
| **GET** | `/api/actors?name=` | Filter actors by (case-insensitive) name |

---

### 3. Testing the API

You can test the API in three ways — **Postman**, **Curl**, or **Swagger**.

#### 🧪 3.1 Testing with Postman (recommended for beginners)
Postman files are located in the `/postman` folder:
- `Movie Database API.postman_collection.json` — main collection
- `movies-api.postman_environment.json` — environment setup

To use:
1. Open **Postman**
2. Import both files
3. Run the entire collection at once or explore requests individually  
   (Each folder corresponds to an entity: *Movies*, *Genres*, *Actors*).

Each request includes pre-filled JSON bodies and response examples for easy validation.

#### 💻 3.2 Testing with Curl (command-line)
A ready-to-use script is located at `/scripts/curl-smoke-tests.sh`.

Run all tests at once:
```bash
bash scripts/curl-smoke-tests.sh
```
Each section of the script corresponds to one of the required tests.  
It automatically performs CRUD operations and prints formatted responses.

#### 🌐 3.3 Testing with Swagger UI (bonus)
Swagger provides an interactive API explorer.

Once the app is running, open your browser at:
```
http://localhost:8080/swagger-ui/index.html
```
You can send live API requests directly from this interface — no external tools needed.

#### 🧭 3.4 Testing Individual Endpoints
A detailed guide for individual endpoint tests is available at:
```
/docs/INDIVIDUAL_TESTS.md
```
It explains how to validate each endpoint step-by-step, matching the school’s review checklist.

---

## 🧰 Additional Features and Bonus Functionality

| Feature | Description |
|----------|--------------|
| **Pagination** | `GET /api/movies?page=0&size=10` returns paged results |
| **Case-insensitive search** | e.g., `/api/actors?name=leonardo` matches “Leonardo” |
| **Force deletion** | e.g., `DELETE /api/genres/1?force=true` removes relationships |
| **Custom Error Handling** | `@ControllerAdvice` centralizes exception management (e.g., 404, 400) |
| **Validation** | `@Valid`, `@NotNull`, and `@Size` annotations prevent invalid input |
| **Sample Data Seeder** | Preloads the database with 5 genres, 20 movies, and 15 actors |

### Example Error Response
```json
{
  "timestamp": "2025-10-07T20:15:43.221",
  "status": 404,
  "error": "Not Found",
  "message": "Movie with ID 99 not found"
}
```

---

### 🧠 Technologies Used
- **Spring Boot** – framework for rapid backend development  
- **Spring Data JPA** – ORM layer to simplify database operations  
- **SQLite** – lightweight embedded database  
- **Lombok** – reduces boilerplate code  
- **Swagger** – visual documentation and live API testing  
- **Postman / Curl** – API testing and validation tools  

---

### ✅ Summary

This project demonstrates how to:
- Build a fully functional REST API with **Spring Boot** and **JPA**
- Manage **Many-to-Many** relationships
- Implement **input validation**, **error handling**, and **pagination**
- Test APIs using **Postman**, **Curl**, and **Swagger**
- Maintain clean, well-structured code following best practices

---

Enjoy exploring your new Movie Database API! 🍿

