package tech.kood.kmdb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.kood.kmdb.model.Actor;
import tech.kood.kmdb.model.Movie;
import tech.kood.kmdb.service.MovieService;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // CRUD

    @PostMapping // 201 + created entity
    public ResponseEntity<Movie> create(@RequestBody Movie movie) {
        Movie created = movieService.create(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping // optional filters ?genre= ?year= ?actor=
    public ResponseEntity<?> findAllOrFiltered(
        @RequestParam(required = false) Long genre,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Long actor) {

            int count = (genre != null ? 1 : 0) + (year != null ? 1: 0) + (actor != null ? 1 : 0);
            if (count > 1) {
                return ResponseEntity.badRequest().body("Use only ONE of: genre, year, actor");
            }

            if (genre != null) return ResponseEntity.ok(movieService.findByGenre(genre));
            if (year != null) return ResponseEntity.ok(movieService.findByYear(year));
            if (actor != null) return ResponseEntity.ok(movieService.findByActor(actor));

            return ResponseEntity.ok(movieService.findAll());
        }

    @GetMapping("/{id}") // 200 or 404
    public ResponseEntity<Movie> findOne(@PathVariable Long id) {
        Optional<Movie> m = movieService.findbyId(id);
        return ResponseEntity.of(m);
    }

    @PatchMapping("/{id}") // 200 + updated entity
    public ResponseEntity<Movie> patch(@PathVariable Long id, @RequestBody Movie partial) {
        return movieService.update(id, partial)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}") // 204
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/actors") // 200 + list (or empty)
    public List<Actor> actorsForMovie(@PathVariable Long id) {
        return movieService.getActorsForMovie(id);
    }
}
