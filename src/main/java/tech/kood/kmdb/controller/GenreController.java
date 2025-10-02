package tech.kood.kmdb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import tech.kood.kmdb.model.Genre;
import tech.kood.kmdb.model.Movie;
import tech.kood.kmdb.service.GenreService;

// CRUD endpoints for Genre
// Extra: Pagination with page and size
@RestController
@RequestMapping("/api/genres")
public class GenreController {
    
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    // CRUD

    @PostMapping // 201 + created entity
    public ResponseEntity<Genre> create(@Valid @RequestBody Genre genre) {
        Genre created = genreService.create(genre);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping // 200 + list
    public ResponseEntity<List<Genre>> findAll() {
        return ResponseEntity.ok(genreService.findAll());
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<?> findAll(Pageable pageable) {
         if (pageable.getPageNumber() < 0 || pageable.getPageSize() < 1 || pageable.getPageSize() > 100) {
            String message = "Invalid pagination parameters: Page must be >= 0 and size must be between 1 and 100.";
            return ResponseEntity.badRequest().body(message);
        }

        Page<Genre> page = genreService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}") // 200 or 404
    public ResponseEntity<Genre> findOne(@PathVariable Long id) {
        Optional<Genre> g = genreService.findbyId(id);
        return ResponseEntity.of(g);
    }

    @PatchMapping("/{id}") // 200 + updated entity
    public ResponseEntity<Genre> updateName(@PathVariable Long id, @Valid @RequestBody UpdateName body) {
        if (body == null || body.name() == null || body.name().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        
        return genreService.updateName(id, body.name())
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}") // ?force=true|false -> 204/400
    public ResponseEntity<Void> delete(@PathVariable Long id,
    @RequestParam(name = "force", defaultValue = "false") boolean force) {
        if (force) {
            genreService.delete(id, true);
        } else {
            genreService.delete(id, false); // enforce default, block if related
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/movies") // 200 + list (or empty)
    public List<Movie> moviesByGenre(@PathVariable Long id) {
        return genreService.findMoviesByGenre(id);
    }

    // Patch { "name" : "New Name"}
    public static record UpdateName(String name) {}
}
