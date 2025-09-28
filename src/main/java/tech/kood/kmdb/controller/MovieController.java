package tech.kood.kmdb.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import jakarta.validation.Valid;
import tech.kood.kmdb.dto.MoviePatchDTO;
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
    public ResponseEntity<Movie> create(@Valid @RequestBody Movie movie) {
        Movie created = movieService.create(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

     @GetMapping("/{id}") // 200 or 404
    public ResponseEntity<Movie> findOne(@PathVariable Long id) {
        Optional<Movie> m = movieService.findbyId(id);
        return ResponseEntity.of(m);
    }

    @PatchMapping("/{id}") // 200 + updated entity
    public ResponseEntity<Movie> patch(@PathVariable Long id, @Valid @RequestBody MoviePatchDTO partial) {
        return movieService.update(id, partial)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}") // ?force= -> 204/400
    public ResponseEntity<Void> delete(@PathVariable Long id,
    @RequestParam(name = "force", defaultValue = "false") boolean force) {
        movieService.delete(id, force);
        return ResponseEntity.noContent().build();
    }

    @GetMapping // optional filters ?genre= ?year= ?actor=
    public ResponseEntity<?> findAllOrFiltered(
        @RequestParam(required = false) Long genre,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Long actor,
        Pageable pageable // ?page=&size=
        ) {
            // Tiny validation
            if (pageable.getPageNumber() < 0 || pageable.getPageSize() < 1 || pageable.getPageSize() > 100) {
                String message = "Invalid pagination parameters: Page must be >= 0 and size must be between 1 and 100.";
                return ResponseEntity.badRequest().body(message);
            }

            int count = (genre != null ? 1 : 0) + (year != null ? 1: 0) + (actor != null ? 1 : 0);
            if (count > 1) {
                return ResponseEntity.badRequest().body("Use only ONE of: genre, year, actor");
            }

            Page<Movie> page =
            genre != null ? movieService.findByGenre(genre, pageable) :
            year != null ? movieService.findByYear(year, pageable) :
            actor != null ? movieService.findByActor(actor, pageable) :
                            movieService.findAll(pageable);

            return ResponseEntity.ok(page);
        }

    @GetMapping("/{id}/actors") // 200 + list (or empty)
    public List<Actor> actorsForMovie(@PathVariable Long id) {
        return movieService.getActorsForMovie(id);
    }

    @GetMapping("/search") // /search?title=matrix&page=0&size=10
    public ResponseEntity<Page<Movie>> searchByTitle(
        @RequestParam String title,
        Pageable pageable
    ) {
        if (pageable.getPageNumber() < 0 || pageable.getPageSize() < 1 || pageable.getPageSize() > 100) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(movieService.searchByTitle(title, pageable));
    }
}
