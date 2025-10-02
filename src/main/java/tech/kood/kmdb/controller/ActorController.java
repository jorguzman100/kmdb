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
import tech.kood.kmdb.model.Actor;
import tech.kood.kmdb.model.Movie;
import tech.kood.kmdb.service.ActorService;

// CRUD endpoints for Actor + Filtering by name.
// Extra: Pagination with page and size
@RestController
@RequestMapping("/api/actors")
public class ActorController {
     
    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    // CRUD

    @PostMapping // 201 + created entity
    public ResponseEntity<Actor> create(@Valid @RequestBody Actor actor) {
        Actor created = actorService.create(actor);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping // 200 + list (optional ?name=)
    public ResponseEntity<List<Actor>> findAllOrByName(@RequestParam(required = false) String name) {
        List<Actor> list = (name != null && !name.isBlank())
        ? actorService.findByNameContainingIgnoreCase(name)
        : actorService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<?> findAllOrByName(
        @RequestParam(required = false) String name,
        Pageable pageable) {

        if (pageable.getPageNumber() < 0 || pageable.getPageSize() < 1 || pageable.getPageSize() > 100) {
            String message = "Invalid pagination parameters: Page must be >= 0 and size must be between 1 and 100.";
            return ResponseEntity.badRequest().body(message);
        }

        Page<Actor> page = (name != null && !name.isBlank()) 
        ? actorService.findByNameContainingIgnoreCase(name, pageable) 
        : actorService.findAll(pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}") // 200 or 404
    public ResponseEntity<Actor> findOne(@PathVariable Long id) {
        Optional<Actor> a = actorService.findbyId(id);
        return ResponseEntity.of(a);
    }

    @PatchMapping("/{id}") // 200 + updated entity
    public ResponseEntity<Actor> patch(@PathVariable Long id, @Valid @RequestBody Actor partial) {
        return actorService.update(id, partial)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}") // ?force= -> 204/400
    public ResponseEntity<Void> delete(@PathVariable Long id,
    @RequestParam(name = "force", defaultValue = "false") boolean force) {
        actorService.delete(id, force);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/movies") // 200 + list (or empty)
    public List<Movie> moviesForActor(@PathVariable Long id) {
        return actorService.getMoviesForActor(id);
    }
}
