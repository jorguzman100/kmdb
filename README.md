# 🎬 Movie Database API

## 📖 Project Overview

The **Movie Database API** is a RESTful application built with **Spring Boot** and **Spring Data JPA**, designed to manage a movie collection.  
It provides endpoints to manage **movies**, **genres**, and **actors**, allowing easy creation, update, search, and deletion of entries while handling relationships between them.

### 💻 Technologies Used
- [**Spring Boot**](https://spring.io/projects/spring-boot) – Open-source Java framework for rapid backend development with great support for REST APIs  
- [**Spring Data JPA**](https://spring.io/projects/spring-data-jpa) – Object-Relational Mapping ORM layer to simplify database operations  
- [**SQLite**](https://sqlite.org/) – lightweight embedded database    
- [**Postman**](https://postman.com) – API testing and validation tools  
- [**Swagger**](https://swagger.io/) – visual documentation and live API testing (bonus) 

## 🧩 Features and Bonus Functionality

| **Feature** | **Description** |
|--------------|-----------------|
| **Full CRUD operations** | Create, read, update, and delete Movies, Genres, and Actors through dedicated endpoints. |
| **Many-to-Many relationships** | Movies ↔ Genres and Movies ↔ Actors. Each movie can belong to multiple genres and feature multiple actors. |
| **Filtering and searching** | Retrieve movies by genre, year, or actor; search actors by name (case-insensitive). |
| **Pagination** | Limit results for large datasets using query parameters like `page` and `size`. Example: `GET /api/movies?page=0&size=10`. |
| **Partial updates (PATCH)** | Update only specific fields of an entity instead of sending the full object. |
| **Force deletion** | Delete an entity with existing relationships using `?force=true` to automatically clear associations. |
| **Custom error handling** | Global exception management with `@ControllerAdvice` provides consistent 400/404 responses and clear messages. |
| **Input validation** | Enforces data integrity with annotations like `@Valid`, `@NotNull`, and `@Size`. Invalid input returns a 400 error. |
| **Sample data seeder** | Automatically loads 5 genres, 20 movies, and 15 actors when the application starts for immediate testing. |
| **SQLite embedded database** | Requires no manual installation or setup. Automatically creates a local file `movies.db`. |
| **Integrated testing tools** | Ready-to-use testing support for Postman, Curl, and Swagger UI. |
| **Swagger UI (bonus)** | Interactive visual documentation allowing users to test API endpoints directly from the browser. |

## ⚙️ Setup and Installation Instructions

### 1. Prerequisites
Before you begin, ensure you have installed:
- **Java 17** or newer 
- **Maven** - No need to manually install it — the project already includes the **Maven Wrapper** (`mvnw` and `mvnw.cmd`), which automatically downloads and uses the correct Maven version for you.
- **SQLite** - No need to install  manually either — it’s an embedded database. The necessary driver is already included, and a new local database file (`movies.db`) will be created automatically when you first run the app.

### 2. Download, Build, and Run
**Download** the submitted project and run it directly with the Maven wrapper — there’s no need for manual compilation.

Run the following command in the project root:
```bash
./mvnw spring-boot:run
```
Once started, the API will be available at:
```
http://localhost:8080
```

### 3. Database 
The app automatically creates a file `movies.db` in your project root on first run.

It seeds the database with 5 genres, 20 movies, and 15 actors.


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

### 2. API Endpoints

| Method | Endpoint | Description |
|--------|-----------|-------------|
| **POST** | `/api/movies` | Create a new movie |
| **GET** | `/api/movies` | List all movies (supports `?genre=`, `?year=`, `?actor=`, `?page=`, `?size=`) |
| **GET** | `/api/movies/{id}` | Get movie by ID |
| **PATCH** | `/api/movies/{id}` | Update movie fields (partial) |
| **DELETE** | `/api/movies/{id}?force=true` | Delete movie, optionally force-remove relationships |
| **GET** | `/api/movies/{movieId}/actors` | List all actors in a movie |
| **GET** | `/api/movies/search?title=` | Search movies by partial title |
| **GET** | `/api/movies?actor={Actor.id}` | Retrieve all movies the actor has starred in |
| **GET** | `/api/genres` | Retrieve all genres |
| **POST** | `/api/genres` | Create a new genre |
| **GET** | `/api/genres/{id}` | Retrieve a genre by ID |
| **PATCH** | `/api/genres/{id}` | Update an existing genre’s name |
| **DELETE** | `/api/genres/{id}?force=true` | Delete a genre (with optional force removal) |
| **GET** | `/api/genres/{id}/movies` | Retrieve all movies belonging to a genre |
| **GET** | `/api/actors` | Retrieve all actors |
| **POST** | `/api/actors` | Create a new actor |
| **GET** | `/api/actors/{id}` | Retrieve an actor by ID |
| **PATCH** | `/api/actors/{id}` | Update actor information |
| **DELETE** | `/api/actors/{id}?force=true` | Delete an actor (with optional force removal) |
| **GET** | `/api/actors?name=` | Filter actors by (case-insensitive) name |


### 3. Testing the API

You can test the API in three ways — **Postman**, **Curl**, or **Swagger**.

#### 🧪 3.1 Testing with Postman
Postman files are located in the `/postman` folder:
- `Movie Database API.postman_collection.json` — main collection
- `movies-api.postman_environment.json` — environment setup

To use:
1. Install the [Postman Desk App](https://www.postman.com/downloads/)
2. Import both files
3. Run the entire collection at once (39 tests)
4. Or explore requests individually  
   - Each folder corresponds to an entity: *Movies*, *Genres*, *Actors*.
   - Each request includes pre-filled JSON bodies and response examples for easy validation.

    For additional reference → [Postman API Testing Tutorial](https://community.postman.com/t/postman-api-testing-tutorial-for-beginner/64511) 


#### 💻 3.2 Testing with Curl (command-line)
A ready-to-use script is located at `/scripts/curl-smoke-tests.sh`.

New file with the test results will be created at `/scripts/curl-test-report.txt`.

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
It explains how to validate each test request with **Postman** / **Curl** / **Swagger**.

---

Thanks for reviewing the Movie Database API! 🍿
