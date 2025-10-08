# Movie Database API — Individual Test Helper
#### (Created with LLM to support the code review)

Uses **exact seed data** (IDs below) for copy/paste-ready tests.
+ curl
+ Swagger 
+ Postman 

**BASE_URL:** `http://localhost:8080`
Swagger UI: `http://localhost:8080/swagger-ui/index.html`

**Seed ID map (fresh DB):**
- Genres → Action=1, Sci-Fi=2, Thriller=3, Drama=4, Comedy=5
- Movies → The Matrix=1, The Matrix Reloaded=2, The Matrix Revolutions=3, Inception=4, The Avengers=5
- Actors → Leonardo DiCaprio=1, Tom Hardy=2, Keanu Reeves=3, Carrie-Anne Moss=4, Laurence Fishburne=5
             Robert Downey Jr.=6, Chris Evans=7, Scarlett Johansson=8, Christian Bale=9

---

**1) Student can describe the structure of a REST API URL, including base URL, resource, and query parameters.**

---

**2) Student can explain the four main HTTP methods used in this project (GET, POST, PATCH, DELETE) and their purposes.**

---

**3) Student can explain the concept of CRUD operations and their importance in database management.**

---

**4) Student can explain what dependency injection is and how it’s used in this project.**

---

**5) Sample data has least 5 genres, 20 movies and 15 actors.**

---

**6) Student can demonstrate various relationship scenarios (movies with multiple genres, actors in multiple movies, and movies with varying numbers of actors).**

---

**7) Student can explain what a JpaRepository is and at least three methods it provides out of the box.**

---

**8) Student can explain the purpose of the @SpringBootApplication annotation.**

---

**9) Spring Boot application runs without errors.**

---

**10) Student can explain the purpose of the @Entity annotation.**

---

**11) Genre entity has id and name fields**

---

**12) Movie entity has id, title, releaseYear, and duration fields**

---

**13) Actor entity has id, name, and birthDate fields**

---

**14) Movie entity uses @ManyToMany to establish the relationship with Genre**

---

**15) Movie and Actor entities use @ManyToMany to establish their relationship**

---

**16) Either Movie or Actor uses @JoinTable to specify the join table for the many-to-many relationship**

---

**17) Student can describe the difference between eager and lazy loading in JPA and which is the default for @ManyToMany relationships**

---

**18) Three repository interfaces exist: GenreRepository, MovieRepository, and ActorRepository**

---

**19) MovieRepository has a custom query method to find movies by genre id**

**curl**
```bash
curl -s "http://localhost:8080/api/movies?genre=1"
```


**Swagger**
GET `/api/movies` → set query param `genre=1` → Execute.


**Postman**
GET `${baseUrl}/api/movies?genre=1`


---

**20) ActorRepository has a custom query method to find actors by name (case insensitive). Test GET /api/actors?name={some_actors_name} with different case variations of actors name to verify case-insensitive search**

**curl**
```bash
curl -s "http://localhost:8080/api/actors?name=leonardo"
curl -s "http://localhost:8080/api/actors?name=LEONARDO"
curl -s "http://localhost:8080/api/actors?name=DiCaprio"
```


**Swagger**
GET `/api/actors` → set query param `name=leonardo` (repeat with `LEONARDO`, `DiCaprio`) → Execute.


**Postman**
GET `${baseUrl}/api/actors?name=leonardo` (duplicate and vary case).


---

**21) Student can describe the role of service layer in a Spring Boot application**

---

**22) Three service classes exist: GenreService, MovieService, and ActorService**

---

**23) Valid birthDate in Actor is correctly handled using ISO 8601 (YYYY-MM-DD). Test POST /api/actors and verify it is stored as YYYY-MM-DD.**

**curl**
```bash
curl -i -X POST "http://localhost:8080/api/actors" -H "Content-Type: application/json" -d '{"name":"Test Actor ISO","birthDate":"1990-05-21"}'
```


**Swagger**
POST `/api/actors` → Body: `{ "name": "Test Actor ISO", "birthDate": "1990-05-21" }` → Execute (expect 201).


**Postman**
POST `${baseUrl}/api/actors` → Body (raw JSON): `{ "name":"Test Actor ISO", "birthDate":"1990-05-21" }` → Send.


---

**24) Actor creation fails with appropriate error when an invalid birthDate is provided. Test POST /api/actors with an invalid birthDate format (e.g., "1990-13-32") and verify it returns a 400 Bad Request status with a clear error message**

**curl**
```bash
curl -i -X POST "http://localhost:8080/api/actors" -H "Content-Type: application/json" -d '{"name":"Bad Date","birthDate":"1990-13-32"}'
```


**Swagger**
POST `/api/actors` with `{ "name": "Bad Date", "birthDate": "1990-13-32" }` → Execute (expect 400).


**Postman**
POST `${baseUrl}/api/actors` with invalid `birthDate` → Send (expect 400).


---

**25) MovieService has a method to get all movies in a specific genre. Verify that GET /api/movies?genre={genreId} returns all movies associated with the specified genre.**

**curl**
```bash
curl -s "http://localhost:8080/api/movies?genre=1"
```


**Swagger**
GET `/api/movies` → `genre=1` → Execute.


**Postman**
GET `${baseUrl}/api/movies?genre=1`


---

**26) MovieService has a method to get all actors in a specific movie. Check that GET /api/movies/{movieId}/actors retrieves all actors starring in the given movie.**

**curl**
```bash
curl -s "http://localhost:8080/api/movies/4/actors"
```


**Swagger**
GET `/api/movies/{id}/actors` with `id=4` → Execute.


**Postman**
GET `${baseUrl}/api/movies/4/actors`


---

**27) ActorService has a method to get all movies an actor has appeared in. Ensure GET /api/movies?actor={Actor.id} returns all movies the specified actor has starred in.**

**curl**
```bash
curl -s "http://localhost:8080/api/movies?actor=1"
```


**Swagger**
GET `/api/movies` → `actor=1` → Execute.


**Postman**
GET `${baseUrl}/api/movies?actor=1`


---

**28) A method exists to update a movie's actors list, handling both adding and removing actors. Test PATCH /api/movies/{id} with a request body containing updated actor information. Verify it returns Status: 200 OK and full, updated movie object.**

**curl**
```bash
curl -i -X PATCH "http://localhost:8080/api/movies/4" -H "Content-Type: application/json" -d '{"actorIds":[1,2]}'
```


**Swagger**
PATCH `/api/movies/{id}` (`id=4`) → Body: `{ "actorIds":[1,2] }` → Execute.


**Postman**
PATCH `${baseUrl}/api/movies/4` → Body: `{ "actorIds":[1,2] }` → Send.


---

**29) Movies can be filtered by release year. Verify that GET /api/movies?year={releaseYear} returns all movies released in the specified year.**

**curl**
```bash
curl -s "http://localhost:8080/api/movies?year=1999"
```


**Swagger**
GET `/api/movies` → `year=1999` → Execute.


**Postman**
GET `${baseUrl}/api/movies?year=1999`


---

**30) id is configured as the primary key on all entities**

---

**31) id field is immutable in PATCH operations. Attempt PATCH requests that try to modify the id field of Genre, Movie, and Actor entities**

**curl**
```bash
curl -i -X PATCH "http://localhost:8080/api/genres/1" -H "Content-Type: application/json" -d '{"id":999,"name":"Action"}'

curl -i -X PATCH "http://localhost:8080/api/actors/1" -H "Content-Type: application/json" -d '{"id":999,"name":"Leonardo DiCaprio"}'

curl -i -X PATCH "http://localhost:8080/api/movies/4" -H "Content-Type: application/json" -d '{"id":999,"title":"Inception"}'
```


**Swagger**
PATCH each entity including an `id` in body → Execute → expect 400/validation error.


**Postman**
Create 3 PATCH requests with `id` in body to verify immutability.


---

**32) GenreService implements full deletion when the force parameter is true. Confirm DELETE /api/genres/{genreId}?force=true removes the genre and associated relationships**

**curl**
```bash
curl -i -X DELETE "http://localhost:8080/api/genres/5"
curl -i -X DELETE "http://localhost:8080/api/genres/5?force=true"
```


**Swagger**
DELETE `/api/genres/{id}` with `id=5` → first without `force` (expect 400), then with `force=true` (expect 204).


**Postman**
DELETE `${baseUrl}/api/genres/5` then `${baseUrl}/api/genres/5?force=true`.


---

**33) Methods that may return null use Optional as return type**

---

**34) Student can explain the difference between GET, POST, PATCH, and DELETE HTTP methods**

---

**35) Three controller classes exist: GenreController, MovieController, and ActorController**

---

**36) POST endpoints return HTTP status 201 (Created) and the created entity. Verify it returns Status: 201 Created and newly created entity object**

**curl**
```bash
curl -i -X POST "http://localhost:8080/api/genres" -H "Content-Type: application/json" -d '{"name":"TestGenre"}'

curl -i -X POST "http://localhost:8080/api/actors" -H "Content-Type: application/json" -d '{"name":"Test Actor","birthDate":"1980-01-01"}'

curl -i -X POST "http://localhost:8080/api/movies" -H "Content-Type: application/json" -d '{"title":"Test Movie","releaseYear":2020,"duration":120}
```


**Swagger**
POST `/api/genres`, `/api/actors`, `/api/movies` with shown bodies → Execute (expect 201).


**Postman**
Three POSTs with same bodies.


---

**37) GET endpoints for retrieving all entities are implemented. Test the complete retrieval of Genre, Movie and Actor entities**

**curl**
```bash
curl -s "http://localhost:8080/api/genres"

curl -s "http://localhost:8080/api/movies"

curl -s "http://localhost:8080/api/actors"
```


**Swagger**
GET `/api/genres`, `/api/movies`, `/api/actors` → Execute.


**Postman**
Three GET requests to the same URLs.


---

**38) PATCH endpoints return HTTP status 200 (OK) and the updated entity**

**curl**
```bash
curl -i -X PATCH "http://localhost:8080/api/genres/4" -H "Content-Type: application/json" -d '{"name":"Drama (Updated)"}'

curl -i -X PATCH "http://localhost:8080/api/movies/1" -H "Content-Type: application/json" -d '{"duration":137}'

curl -i -X PATCH "http://localhost:8080/api/actors/2" -H "Content-Type: application/json" -d '{"name":"Tom Hardy (Updated)"}
```


**Swagger**
PATCH `/api/genres/{id}` (`id=4`), `/api/movies/{id}` (`id=1`), `/api/actors/{id}` (`id=2`) → Execute (expect 200).


**Postman**
Three PATCHs with shown bodies.


---

**39) DELETE endpoints return HTTP status 204 (No Content) on successful deletion**

**curl**
```bash
curl -i -X POST "http://localhost:8080/api/genres" -H "Content-Type: application/json" -d '{"name":"TempDelete"}'
curl -i -X DELETE "http://localhost:8080/api/genres/6"
```


**Swagger**
POST `/api/genres` with name `TempDelete` → note returned `id` (likely next) → DELETE `/api/genres/{thatId}` → expect 204.


**Postman**
POST then DELETE the returned id.


---

**40) Error handling returns HTTP status 404 (Not Found) when an entity is not found**

**curl**
```bash
curl -i "http://localhost:8080/api/genres/999999"

curl -i "http://localhost:8080/api/movies/999999"

curl -i "http://localhost:8080/api/actors/999999"
```


**Swagger**
GET each `/api/.../{id}` with a non-existent id → Execute (expect 404).


**Postman**
Three GETs with clearly non-existent ids.


---

**41) Student can explain the purpose of the application.properties file in a Spring Boot project. Ask the student to explain the contents of application.properties file for SQLite configuration**

---

**42) SQLite JDBC driver dependency with the correct version is added in pom.xml**

---

**43) Student can explain the importance of input validation in API development**

---

**44) Proper error handling is implemented using @ControllerAdvice or similar. Ask the student to explain their approach to error handling**

---

**45) ResourceNotFoundException or similar exists for handling cases where requested data isn't found**

---

**46) Input validation is implemented using Bean Validation annotations. Ask the student to explain their approach to input validation**

---

**47) Student can explain what a 404 HTTP status code means and when it should be used in this project**

---

**48) Student can describe the purpose of the @Valid annotation in controller methods**

---

**49) A Postman collection named Movie Database API exists**

---

**50) Student can explain the benefits of using collections for organized API testing**

---

**51) Student can explain the purpose of HTTP status codes in API responses**

---

**52) A new movie with associated genre and actors can be created in a single POST request. Verify it returns Status: 201 Created and newly created entity object**

**curl**
```bash
curl -i -X POST "http://localhost:8080/api/movies" -H "Content-Type: application/json" -d '{"title":"The Matrix", "releaseYear":1999, "duration":136, "genreIds":[1,2], "actorIds":[3,4]}'
```


**Swagger**
POST `/api/movies` → Body: `{ "title":"The Matrix", "releaseYear":1999, "duration":136, "genreIds":[1,2], "actorIds":[3,4] }` → Execute (expect 201).


**Postman**
POST the same JSON body.


---

**53) A movie's actors list can be updated using a PATCH request, both adding and removing actors. Verify it returns Status: 200 OK and updated entity object**

**curl**
```bash
curl -i -X PATCH "http://localhost:8080/api/movies/1" -H "Content-Type: application/json" -d '{"actorIds":[3,5]}'
```


**Swagger**
PATCH `/api/movies/{id}` with `id=1` → Body: `{ "actorIds":[3,5] }` → Execute (expect 200).


**Postman**
PATCH the same body.


---

**54) The default behaviour when attempting to delete a resource with an existing relationship is demonstrated. Operation must not be completed. Response code 400 and descriptive Response body are returned**

**curl**
```bash
curl -i -X DELETE "http://localhost:8080/api/genres/1"
```


**Swagger**
DELETE `/api/genres/{id}` with `id=1` (has relationships) → Execute (expect 400 with message).


**Postman**
DELETE `${baseUrl}/api/genres/1` (expect 400).


---

**55) Error handling can be demonstrated by attempting to retrieve a non-existent entity and checking for a 404 status code**

**curl**
```bash
curl -i "http://localhost:8080/api/movies/42424242"
```


**Swagger**
GET `/api/movies/{id}` with a non-existent id (e.g., 42424242) → Execute (expect 404).


**Postman**
GET `${baseUrl}/api/movies/42424242`.


---

**56) Creating an entity with invalid data (e.g., null title for a movie) results in a 400 status code**

**curl**
```bash
curl -i -X POST "http://localhost:8080/api/movies" -H "Content-Type: application/json" -d '{"title":null,"releaseYear":2020,"duration":120}'
```


**Swagger**
POST `/api/movies` with `{ "title": null, "releaseYear": 2020, "duration": 120 }` → Execute (expect 400).


**Postman**
POST with the same invalid body (expect 400).


---

**57) Force deleting an actor (force=true) removes it from associated movies and deletes the actor**

**curl**
```bash
curl -i -X DELETE "http://localhost:8080/api/actors/2?force=true"
```


**Swagger**
DELETE `/api/actors/{id}?force=true` with `id=2` → Execute (expect 204).


**Postman**
DELETE `${baseUrl}/api/actors/2?force=true`.


---

**58) The README file contains a clear project overview, setup instructions, and usage guide**

---

**59) The code is well-organized, properly commented, and follows best practices for the chosen programming language(s)**

---

**60) Pagination is implemented for GET requests that return multiple entities. Verify GET /api/movies?page=0&size=10 returns the first 10 movies**

**curl**
```bash
curl -s "http://localhost:8080/api/movies?page=0&size=10"
```


**Swagger**
GET `/api/movies` with `page=0`, `size=10` → Execute.


**Postman**
GET `${baseUrl}/api/movies?page=0&size=10`.


---

**61) Proper error handling and input validation for invalid pagination parameters is implemented. Test GET /api/movies?page=-1&size=99999 and verify it returns an appropriate error response**

**curl**
```bash
curl -i "http://localhost:8080/api/movies?page=-1&size=99999"
```


**Swagger**
GET `/api/movies` with `page=-1`, `size=99999` → Execute (expect 400).


**Postman**
GET `${baseUrl}/api/movies?page=-1&size=99999`.


---

**62) Simple search functionality for movies by title is implemented. Confirm GET /api/movies/search?title=matrix returns movies with "matrix" (or any other similar example) in the title**

**curl**
```bash
curl -s "http://localhost:8080/api/movies/search?title=matrix"
```


**Swagger**
GET `/api/movies/search` with `title=matrix` → Execute.


**Postman**
GET `${baseUrl}/api/movies/search?title=matrix`.


---

**63) Case-insensitive and partial match search for movie titles is implemented. Test GET /api/movies/search?title=star with various case combinations and partial words**

**curl**
```bash
curl -s "http://localhost:8080/api/movies/search?title=star"

curl -s "http://localhost:8080/api/movies/search?title=STAR"

curl -s "http://localhost:8080/api/movies/search?title=Sta"
```


**Swagger**
GET `/api/movies/search` with `title=star`, then `STAR`, then `Sta` → Execute.


**Postman**
Three GETs with those titles.


---

**64) Student has implemented additional technologies, security enhancements and/or features beyond the core requirements**

---
