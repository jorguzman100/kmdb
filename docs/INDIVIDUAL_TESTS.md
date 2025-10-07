# Movie Database API — Individual Test Helper (1–64)

Copy/paste-friendly tests for **cURL**, plus matching steps for **Swagger UI** and **Postman**.
Assumes app at `http://localhost:8080` with seed data loaded.

> Tip: To avoid manual ID lookups, cURL snippets auto-resolve IDs using names via `jq`. In Postman, you can **Import → Raw Text** and paste the cURL.

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

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
cURL:
```bash
BASE="http://localhost:8080"
ACTION_ID=$(curl -s "$BASE/api/genres" | jq -r '.[] | select(.name=="Action") | .id' | head -n1)
curl -s "$BASE/api/movies?genre=$ACTION_ID"
```
Swagger: GET `/api/genres` → copy id for **Action** → GET `/api/movies` with `genre=<id>` → Execute.
Postman: Import the cURL above (Import → Raw Text → Continue → Import). Or manually: GET `${baseUrl}/api/movies?genre=<ActionId>`.

---

**20) ActorRepository has a custom query method to find actors by name (case insensitive). Test GET /api/actors?name={some_actors_name} with different case variations of actors name to verify case-insensitive search**
cURL:
```bash
BASE="http://localhost:8080"
curl -s "$BASE/api/actors?name=leonardo diCaprio"
curl -s "$BASE/api/actors?name=LEONARDO DICAPRIO"
curl -s "$BASE/api/actors?name=LeoNArdo"
```
Swagger: GET `/api/actors` with `name` set to different case variants (e.g., `leonardo`, `LEONARDO`).
Postman: Create GET `${baseUrl}/api/actors` with Query Param `name` = `leonardo`, duplicate tab with `LEONARDO`, etc., or import cURL.

---

**21) Student can describe the role of service layer in a Spring Boot application**

---

**22) Three service classes exist: GenreService, MovieService, and ActorService**

---

**23) Valid birthDate in Actor is correctly handled using ISO 8601 (YYYY-MM-DD). Test POST /api/actors and verify it is stored as YYYY-MM-DD.**
cURL:
```bash
curl -i -X POST "http://localhost:8080/api/actors" \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Actor ISO","birthDate":"1990-05-21"}'
```
Swagger: POST `/api/actors` → Body (application/json) with `{ "name": "Test Actor ISO", "birthDate": "1990-05-21" }` → Execute. Expect 201.
Postman: POST `${baseUrl}/api/actors` → Body: raw JSON `{ "name":"Test Actor ISO", "birthDate":"1990-05-21" }` → Send. Expect 201.

---

**24) Actor creation fails with appropriate error when an invalid birthDate is provided. Test POST /api/actors with an invalid birthDate format (e.g., "1990-13-32") and verify it returns a 400 Bad Request status with a clear error message**
cURL:
```bash
curl -i -X POST "http://localhost:8080/api/actors" \
  -H "Content-Type: application/json" \
  -d '{"name":"Bad Date","birthDate":"1990-13-32"}'
```
Swagger: POST `/api/actors` with invalid `birthDate` (e.g., `1990-13-32`) → Execute. Expect 400 with validation error.
Postman: POST `${baseUrl}/api/actors` → Body: `{ "name":"Bad Date", "birthDate":"1990-13-32" }` → Send. Expect 400.

---

**25) MovieService has a method to get all movies in a specific genre. Verify that GET /api/movies?genre={genreId} returns all movies associated with the specified genre.**
cURL:
```bash
BASE="http://localhost:8080"
ACTION_ID=$(curl -s "$BASE/api/genres" | jq -r '.[] | select(.name=="Action") | .id' | head -n1)
curl -s "$BASE/api/movies?genre=$ACTION_ID"
```
Swagger: GET `/api/movies` with Query Param `genre=<ActionId>` → Execute.
Postman: GET `${baseUrl}/api/movies?genre=<ActionId>`; or import cURL above.

---

**26) MovieService has a method to get all actors in a specific movie. Check that GET /api/movies/{movieId}/actors retrieves all actors starring in the given movie.**
cURL:
```bash
BASE="http://localhost:8080"
INCEPTION_ID=$(curl -s "$BASE/api/movies" | jq -r '.[] | select(.title=="Inception") | .id' | head -n1)
curl -s "$BASE/api/movies/$INCEPTION_ID/actors"
```
Swagger: GET `/api/movies/{id}/actors` with `id` of Inception → Execute.
Postman: GET `${baseUrl}/api/movies/{id}/actors` with `id` = Inception id (find via GET `/api/movies`), or import cURL.

---

**27) ActorService has a method to get all movies an actor has appeared in. Ensure GET /api/movies?actor={Actor.id} returns all movies the specified actor has starred in.**
cURL:
```bash
BASE="http://localhost:8080"
LEO_ID=$(curl -s "$BASE/api/actors?name=leonardo%20dicaprio" | jq -r '.[0].id')
curl -s "$BASE/api/movies?actor=$LEO_ID"
```
Swagger: GET `/api/movies` with Query Param `actor=<actorId>` (e.g., Leonardo DiCaprio id) → Execute.
Postman: GET `${baseUrl}/api/movies?actor=<actorId>`; obtain `actorId` by GET `/api/actors?name=leonardo%20dicaprio`, or import cURL.

---

**28) A method exists to update a movie's actors list, handling both adding and removing actors. Test PATCH /api/movies/{id} with a request body containing updated actor information. Verify it returns Status: 200 OK and full, updated movie object.**
cURL:
```bash
BASE="http://localhost:8080"
INCEPTION_ID=$(curl -s "$BASE/api/movies" | jq -r '.[] | select(.title=="Inception") | .id' | head -n1)
LEO_ID=$(curl -s "$BASE/api/actors?name=leonardo%20dicaprio" | jq -r '.[0].id')
TOM_ID=$(curl -s "$BASE/api/actors?name=tom%20hardy" | jq -r '.[0].id')
curl -i -X PATCH "$BASE/api/movies/$INCEPTION_ID" \
  -H "Content-Type: application/json" \
  -d "{\"actorIds\":[\"$LEO_ID\",\"$TOM_ID\"]}"
```
Swagger: PATCH `/api/movies/{id}` with body `{ "actorIds": [<ids>] }` → Execute. Expect 200 with updated movie.
Postman: PATCH `${baseUrl}/api/movies/{id}` → Body JSON `{ "actorIds":[<ids>] }`; or import cURL above.

---

**29) Movies can be filtered by release year. Verify that GET /api/movies?year={releaseYear} returns all movies released in the specified year.**
cURL:
```bash
curl -s "http://localhost:8080/api/movies?year=1999"
```
Swagger: GET `/api/movies` with Query Param `year=1999` → Execute.
Postman: GET `${baseUrl}/api/movies?year=1999`.

---

**30) id is configured as the primary key on all entities**

---

**31) id field is immutable in PATCH operations. Attempt PATCH requests that try to modify the id field of Genre, Movie, and Actor entities**
cURL:
```bash
BASE="http://localhost:8080"
GENRE_ID=$(curl -s "$BASE/api/genres" | jq -r '.[] | select(.name=="Action") | .id' | head -n1)
curl -i -X PATCH "$BASE/api/genres/$GENRE_ID" -H "Content-Type: application/json" -d '{"id":999,"name":"Action"}'

ACTOR_ID=$(curl -s "$BASE/api/actors?name=leonardo%20dicaprio" | jq -r '.[0].id')
curl -i -X PATCH "$BASE/api/actors/$ACTOR_ID" -H "Content-Type: application/json" -d '{"id":999,"name":"Leonardo DiCaprio"}'

MOVIE_ID=$(curl -s "$BASE/api/movies" | jq -r '.[] | select(.title=="Inception") | .id' | head -n1)
curl -i -X PATCH "$BASE/api/movies/$MOVIE_ID" -H "Content-Type: application/json" -d '{"id":999,"title":"Inception"}'
```
Swagger: Attempt PATCH on each entity including an `id` field in body → Execute → expect 400/validation error.
Postman: PATCH endpoints with body including `id` to verify immutability, or import cURL above.

---

**32) GenreService implements full deletion when the force parameter is true. Confirm DELETE /api/genres/{genreId}?force=true removes the genre and associated relationships**
cURL:
```bash
BASE="http://localhost:8080"
COMEDY_ID=$(curl -s "$BASE/api/genres" | jq -r '.[] | select(.name=="Comedy") | .id' | head -n1)
curl -i -X DELETE "$BASE/api/genres/$COMEDY_ID"
curl -i -X DELETE "$BASE/api/genres/$COMEDY_ID?force=true"
```
Swagger: DELETE `/api/genres/{id}` (first without `force`, then with `force=true`) → Execute. Expect 400 then 204.
Postman: DELETE `${baseUrl}/api/genres/{id}` (no `force`) then `${baseUrl}/api/genres/{id}?force=true`.

---

**33) Methods that may return null use Optional as return type**

---

**34) Student can explain the difference between GET, POST, PATCH, and DELETE HTTP methods**

---

**35) Three controller classes exist: GenreController, MovieController, and ActorController**

---

**36) POST endpoints return HTTP status 201 (Created) and the created entity. Verify it returns Status: 201 Created and newly created entity object**
cURL:
```bash
curl -i -X POST "http://localhost:8080/api/genres" -H "Content-Type: application/json" -d '{"name":"TestGenre"}'
curl -i -X POST "http://localhost:8080/api/actors" -H "Content-Type: application/json" -d '{"name":"Test Actor","birthDate":"1980-01-01"}'
curl -i -X POST "http://localhost:8080/api/movies" -H "Content-Type: application/json" -d '{"title":"Test Movie","releaseYear":2020,"duration":120}'
```
Swagger: POST each of `/api/genres`, `/api/actors`, `/api/movies` with shown bodies → Execute. Expect 201.
Postman: Create three POST requests with the same bodies, or import the cURL above.

---

**37) GET endpoints for retrieving all entities are implemented. Test the complete retrieval of Genre, Movie and Actor entities**
cURL:
```bash
curl -s "http://localhost:8080/api/genres"
curl -s "http://localhost:8080/api/movies"
curl -s "http://localhost:8080/api/actors"
```
Swagger: GET `/api/genres`, `/api/movies`, `/api/actors` → Execute.
Postman: Create three GET requests to the same URLs, or import the cURL above.

---

**38) PATCH endpoints return HTTP status 200 (OK) and the updated entity**
cURL:
```bash
BASE="http://localhost:8080"
GENRE_ID=$(curl -s "$BASE/api/genres" | jq -r '.[] | select(.name=="Drama") | .id' | head -n1)
curl -i -X PATCH "$BASE/api/genres/$GENRE_ID" -H "Content-Type: application/json" -d '{"name":"Drama (Updated)"}'

MOVIE_ID=$(curl -s "$BASE/api/movies" | jq -r '.[] | select(.title=="The Matrix") | .id' | head -n1)
curl -i -X PATCH "$BASE/api/movies/$MOVIE_ID" -H "Content-Type: application/json" -d '{"duration":137}'

ACTOR_ID=$(curl -s "$BASE/api/actors?name=tom%20hardy" | jq -r '.[0].id')
curl -i -X PATCH "$BASE/api/actors/$ACTOR_ID" -H "Content-Type: application/json" -d '{"name":"Tom Hardy (Updated)"}'
```
Swagger: PATCH `/api/genres/{id}` (name), `/api/movies/{id}` (duration), `/api/actors/{id}` (name) → Execute. Expect 200.
Postman: Create three PATCH requests with shown bodies; or import the cURL above.

---

**39) DELETE endpoints return HTTP status 204 (No Content) on successful deletion**
cURL:
```bash
curl -i -X POST "http://localhost:8080/api/genres" -H "Content-Type: application/json" -d '{"name":"TempDelete"}'
GEN_ID=$(curl -s "http://localhost:8080/api/genres" | jq -r '.[] | select(.name=="TempDelete") | .id' | head -n1)
curl -i -X DELETE "http://localhost:8080/api/genres/$GEN_ID"
```
Swagger: POST `/api/genres` TempDelete → copy id → DELETE `/api/genres/{id}` → Expect 204.
Postman: POST then DELETE as above; or import cURL.

---

**40) Error handling returns HTTP status 404 (Not Found) when an entity is not found**
cURL:
```bash
curl -i "http://localhost:8080/api/genres/999999"
curl -i "http://localhost:8080/api/movies/999999"
curl -i "http://localhost:8080/api/actors/999999"
```
Swagger: GET each `/api/.../{id}` with a non-existent id → Execute. Expect 404.
Postman: Create three GETs with high non-existent ids; or import cURL.

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
cURL:
```bash
BASE="http://localhost:8080"
ACTION_ID=$(curl -s "$BASE/api/genres" | jq -r '.[] | select(.name=="Action") | .id' | head -n1)
SCIFI_ID=$(curl -s "$BASE/api/genres" | jq -r '.[] | select(.name=="Sci-Fi") | .id' | head -n1)
KEANU_ID=$(curl -s "$BASE/api/actors?name=keanu%20reeves" | jq -r '.[0].id')
CARRIE_ID=$(curl -s "$BASE/api/actors?name=carrie-anne%20moss" | jq -r '.[0].id')
curl -i -X POST "$BASE/api/movies" \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"The Matrix\",\"releaseYear\":1999,\"duration\":136,\"genreIds\":[$ACTION_ID,$SCIFI_ID],\"actorIds\":[$KEANU_ID,$CARRIE_ID]}"
```
Swagger: POST `/api/movies` with JSON body including `genreIds` and `actorIds` resolved to real IDs → Execute. Expect 201.
Postman: POST `${baseUrl}/api/movies` with the same JSON as above (get IDs via GETs or import cURL).

---

**53) A movie's actors list can be updated using a PATCH request, both adding and removing actors. Verify it returns Status: 200 OK and updated entity object**
cURL:
```bash
BASE="http://localhost:8080"
MATRIX_ID=$(curl -s "$BASE/api/movies" | jq -r '.[] | select(.title=="The Matrix") | .id' | head -n1)
KEANU_ID=$(curl -s "$BASE/api/actors?name=keanu%20reeves" | jq -r '.[0].id')
FISH_ID=$(curl -s "$BASE/api/actors?name=laurence%20fishburne" | jq -r '.[0].id')
curl -i -X PATCH "$BASE/api/movies/$MATRIX_ID" \
  -H "Content-Type: application/json" \
  -d "{\"actorIds\":[$KEANU_ID,$FISH_ID]}"
```
Swagger: PATCH `/api/movies/{id}` with `{ "actorIds": [<id1>,<id2>] }` → Execute. Expect 200.
Postman: PATCH `${baseUrl}/api/movies/{id}` with body showing the updated `actorIds`, or import cURL.

---

**54) The default behaviour when attempting to delete a resource with an existing relationship is demonstrated. Operation must not be completed. Response code 400 and descriptive Response body are returned**
cURL:
```bash
BASE="http://localhost:8080"
ACTION_ID=$(curl -s "$BASE/api/genres" | jq -r '.[] | select(.name=="Action") | .id' | head -n1)
curl -i -X DELETE "$BASE/api/genres/$ACTION_ID"
```
Swagger: DELETE `/api/genres/{id}` (with existing relationships) without `force` → Execute. Expect 400.
Postman: DELETE `${baseUrl}/api/genres/{id}` (for a related genre like Action).

---

**55) Error handling can be demonstrated by attempting to retrieve a non-existent entity and checking for a 404 status code**
cURL:
```bash
curl -i "http://localhost:8080/api/movies/42424242"
```
Swagger: GET `/api/movies/{id}` with non-existent id → Execute. Expect 404.
Postman: GET `${baseUrl}/api/movies/42424242`.

---

**56) Creating an entity with invalid data (e.g., null title for a movie) results in a 400 status code**
cURL:
```bash
curl -i -X POST "http://localhost:8080/api/movies" -H "Content-Type: application/json" -d '{"title":null,"releaseYear":2020,"duration":120}'
```
Swagger: POST `/api/movies` with invalid body (e.g., `title=null`) → Execute. Expect 400.
Postman: POST `${baseUrl}/api/movies` with invalid body `{ "title": null, "releaseYear": 2020, "duration": 120 }`.

---

**57) Force deleting an actor (force=true) removes it from associated movies and deletes the actor**
cURL:
```bash
BASE="http://localhost:8080"
TOM_ID=$(curl -s "$BASE/api/actors?name=tom%20hardy" | jq -r '.[0].id')
curl -i -X DELETE "$BASE/api/actors/$TOM_ID?force=true"
```
Swagger: DELETE `/api/actors/{id}` with `force=true` → Execute. Expect 204 and relationships removed.
Postman: DELETE `${baseUrl}/api/actors/{id}?force=true` (for Tom Hardy id), or import cURL.

---

**58) The README file contains a clear project overview, setup instructions, and usage guide**

---

**59) The code is well-organized, properly commented, and follows best practices for the chosen programming language(s)**

---

**60) Pagination is implemented for GET requests that return multiple entities. Verify GET /api/movies?page=0&size=10 returns the first 10 movies**
cURL:
```bash
curl -s "http://localhost:8080/api/movies?page=0&size=10"
```
Swagger: GET `/api/movies` with `page=0`, `size=10` → Execute.
Postman: GET `${baseUrl}/api/movies?page=0&size=10`.

---

**61) Proper error handling and input validation for invalid pagination parameters is implemented. Test GET /api/movies?page=-1&size=99999 and verify it returns an appropriate error response**
cURL:
```bash
curl -i "http://localhost:8080/api/movies?page=-1&size=99999"
```
Swagger: GET `/api/movies` with invalid `page=-1`, `size=99999` → Execute. Expect 400.
Postman: GET `${baseUrl}/api/movies?page=-1&size=99999`.

---

**62) Simple search functionality for movies by title is implemented. Confirm GET /api/movies/search?title=matrix returns movies with "matrix" (or any other similar example) in the title**
cURL:
```bash
curl -s "http://localhost:8080/api/movies/search?title=matrix"
```
Swagger: GET `/api/movies/search` with `title=matrix` (optionally add `page`/`size`) → Execute.
Postman: GET `${baseUrl}/api/movies/search?title=matrix`.

---

**63) Case-insensitive and partial match search for movie titles is implemented. Test GET /api/movies/search?title=star with various case combinations and partial words**
cURL:
```bash
curl -s "http://localhost:8080/api/movies/search?title=star"
curl -s "http://localhost:8080/api/movies/search?title=STAR"
curl -s "http://localhost:8080/api/movies/search?title=Sta"
```
Swagger: GET `/api/movies/search` with `title` variants: `star`, `STAR`, `Sta` → Execute.
Postman: GET three requests with the same variants; or import cURL above.

---

**64) Student has implemented additional technologies, security enhancements and/or features beyond the core requirements**

---
