# Movie Database API 🎬

A simple REST API built with **Spring Boot** and **JPA** to manage a movie collection.  
It allows storing and retrieving information about **movies, genres, and actors**, including their relationships.  

---

## 🚀 Project Overview

This project follows a **layered architecture** for clarity and maintainability:

### Core Layers

- **Controller**  
Receives requests, call services, and return responses through the REST API endpoints (`/api/movies`, `/api/actors`, `/api/genres`). 

- **Service**  
Contains the business logic. Services call repositories and apply rules (e.g., updating actors in a movie, handling force deletion).

- **Repository**  
Handles direct communication with the database (SQLite).

- **Model**  
Defines the entities (Genre, Movie, Actor) with their fields and relationships.


### Supporting Components

- **DTO**  
  Used for updates (`PATCH`) so you only send/receive needed fields instead of full entities.

- **Seeder**  
  Automatically loads sample data (genres, movies, actors) into the database when the app starts. You don’t need to add data manually.

- **Exception Handling**  
  Centralized error handling with custom exceptions (e.g., `ResourceNotFoundException`). Ensures clear error messages and proper HTTP status codes.

---

## ✨ Features

### CRUD operations
- Create a movie:
  ```http
  POST /api/movies
  {
    "title": "Inception",
    "releaseYear": 2010,
    "duration": 148,
    "genres": [1, 2],
    "actors": [1, 2]
  }
  ```
- Get all genres: `GET /api/genres`
- Update an actor’s name:
  ```http
  PATCH /api/actors/3
  {
    "name": "Leonardo Wilhelm DiCaprio"
  }
  ```
- Delete a movie: `DELETE /api/movies/5`

### Relationships
- A movie can belong to multiple genres: *“Inception” → Action, Sci-Fi, Thriller*.  
- An actor can act in multiple movies: *Tom Hanks → Forrest Gump, Cast Away, The Terminal*.  

### Filtering
- By genre: `GET /api/movies?genre=1`
- By year: `GET /api/movies?year=2010`
- By actor: `GET /api/movies?actor=5`

### Search
- Movies by title: `GET /api/movies/search?title=matrix`
- Actors by name: `GET /api/actors?name=streep`

### Pagination
- Get 10 movies at a time: `GET /api/movies?page=0&size=10`

### Partial Updates (PATCH)
- Only update one field:
  ```http
  PATCH /api/movies/2
  {
    "duration": 120
  }
  ```

### Force Deletion
- Normal delete fails if related:
  ```http
  DELETE /api/genres/1
  → 400 Cannot delete genre 'Action' because it has 15 associated movies
  ```
- Force delete:
  ```http
  DELETE /api/genres/1?force=true
  → 204 No Content
  ```

### Error Handling
- Example: `GET /api/actors/999` →
  ```json
  {
    "error": "Actor not found"
  }
  ```
  Status: `404 Not Found`

### Validation
- Invalid date:
  ```http
  POST /api/actors
  {
    "name": "John Doe",
    "birthDate": "1990-13-32"
  }
  ```
  Response: `400 Bad Request` with error message.

---

## ⚙️ Setup & Installation

1. Navigate into the project folder:
   ```bash
   cd kmdb
   ```

2. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

3. The API starts at:
   ```
   http://localhost:8080
   ```

4. **Database & Sample Data**  
   - Uses **SQLite** (`movies.db`).  
   - Sample data (5+ genres, 20+ movies, 15+ actors) is automatically loaded.  
   - You don’t need to do anything extra.  
   - To reset the database, simply delete the file and restart:
     ```bash
     rm movies.db
     ./mvnw spring-boot:run
     ```

5. **Testing Tool: Postman**  

   - Postman is the easiest way to run and test all endpoints.  
   - Free, works in browser or desktop app.  
   - Tutorial for beginners → [Postman API Testing Tutorial](https://community.postman.com/t/postman-api-testing-tutorial-for-beginner/64511)      

    - On Collections, click "Import" button.
    - Drag and drop the collection and environment files from the`postman/` folder. 
    - Click on `Collections/Movie Database API`.
    - On the top-right-corner select the environment `movies-api`.
    - Hover on the `Collections/Movie Database API`, click on the 3 dots and press`Run`.
    - Select `Run manually` and click `Run Movie Database API`.
       
      → If everything went well, you will see the 39 tests with "PASS" results.
---

## 📚 Usage Guide

### Base URL
```
http://localhost:8080/api
```

### How to use the endpoints
- `POST` → Create a new record (send JSON body).  
- `GET` → Retrieve records (all or filtered).  
- `PATCH` → Update part of a record.  
- `DELETE` → Remove a record (`?force=true` to override relationships).  

👉 Examples:  
- `GET /api/movies` → all movies  
- `GET /api/movies?genre=2` → movies in genre 2  
- `GET /api/movies/search?title=matrix` → search movies by title  
- `PATCH /api/actors/3` → update actor with id 3  

### Testing with Postman
1. Open the Postman collection.  
2. **Run all tests at once** → click **Run Collection**. Example: runs CRUD, filtering, search, pagination tests automatically.  
3. **Try individual endpoints** → select one and press the **▶ Send** button. Example: send `GET /api/movies?year=2010`.  
4. **Play with parameters**:  
   - `GET /api/movies?year=2010` → movies released in 2010  
   - `GET /api/movies?genre=1` → movies in genre 1  
   - `GET /api/movies?actor=5` → movies of actor 5  
   - `GET /api/actors?name=tom` → actors with name “Tom”  

### Testing with Curl (Alternative to Postman)
A ready-made script runs all tests automatically:

```bash
sh scripts/curl-smoke-tests.sh
```

This script will:
- Create sample genres, actors, and movies.
- Verify CRUD, filtering, search, pagination.
- Check error handling and validations.
- Print ✅ PASS or ❌ FAIL results for each test (T01, T02, …).

- Save the results into:
  ```
  scripts/curl-test-report.txt
  ```

👉 If you want to reset the DB before running again:
```bash
rm movies.db
./mvnw spring-boot:run
```

---

## ➕ Additional Features (with examples)

### Extra Requirements
- **Pagination**  
  `GET /api/movies?page=0&size=10` → returns first 10 movies.  
- **Basic Search**  
  `GET /api/movies/search?title=matrix` → finds *The Matrix* and other matching movies.  
- **Error Handling**  
  `GET /api/actors/999` → returns `404 Not Found` with `{ "error": "Actor not found" }`.  
- **Partial Search**  
  `GET /api/actors?name=ste` → finds *Meryl Streep*, *Steve Carell*, etc.  

### Bonus: Swagger 📝
- **What is it?** → Swagger generates interactive API docs.  
- **Why is it helpful?** → Lets you try endpoints directly in your browser without Postman.  
- **How to use it?**  
  1. Start the app.  
  2. Go to:
     ```
     http://localhost:8080/swagger-ui.html
     ```  
  3. Example:  
     - Expand **GET /api/movies**.  
     - Click **“Try it out”** → then **Execute**.  
     - The list of movies will appear directly in the browser.
