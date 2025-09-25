package tech.kood.kmdb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tech.kood.kmdb.model.Genre;
import tech.kood.kmdb.model.Movie;
import tech.kood.kmdb.service.GenreService;

@RestController
@RequestMapping("/api/genres")
public class GenreController {
    
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    // CRUD

    @PostMapping // 201 + created entity
    public ResponseEntity<Genre> create(@RequestBody Genre genre) {
        Genre created = genreService.create(genre);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping // 200 + list
    public List<Genre> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/{id}") // 200 or 404
    public ResponseEntity<Genre> findOne(@PathVariable Long id) {
        Optional<Genre> g = genreService.findbyId(id);
        return ResponseEntity.of(g);
    }

    @PatchMapping("/{id}") // 200 + updated entity
    public ResponseEntity<Genre> updateName(@PathVariable Long id, @RequestBody UpdateName body) {
        return genreService.updateName(id, body.name())
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}") // 204
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        genreService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/movies") // 200 + list (or empty)
    public List<Movie> moviesByGenre(@PathVariable Long id) {
        return genreService.findMoviesByGenre(id);
    }

    // Patch { "name" : "New Name"}
    public static record UpdateName(String name) {}
}
