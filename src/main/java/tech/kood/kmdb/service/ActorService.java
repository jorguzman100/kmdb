package tech.kood.kmdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.kmdb.exception.ResourceNotFoundException;
import tech.kood.kmdb.model.Actor;
import tech.kood.kmdb.model.Movie;
import tech.kood.kmdb.repository.ActorRepository;
import tech.kood.kmdb.repository.MovieRepository;

// Business logic: CRUD and handle relationships
// Extra: Pagination
@Service
public class ActorService {

    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository; // To update owning side on force delete

    // Spring will inject the repository
    public ActorService(ActorRepository actorRepository, MovieRepository movieRepository) {
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
    }

    // CRUD

    @Transactional
    public Actor create(Actor actor) {
        return actorRepository.save(actor);
    }

    @Transactional(readOnly = true)
    public List<Actor> findAll() {
        return actorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Actor> findAll(Pageable pageable) {
        return actorRepository.findAll(pageable); 
    }

    @Transactional(readOnly = true)
    public Optional<Actor> findbyId(Long id) { // Return Optional instead of null if not found
        return actorRepository.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        actorRepository.deleteById(id);
    }

    // Force delete
    @Transactional
    public void delete(Long id, boolean force) {
        Actor actor = actorRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Actor " + id + " not found"));

        int related = actor.getMovies().size();
        if (!force && related > 0) {
            String name = actor.getName();
            throw new IllegalArgumentException("Cannot delete actor '" + name + "' because it has " + related + " associated movies."); // 400 The GlobalException handler maps IllegalArgumentException
        }

        if (force) { // Detach from owning side and make a copy to avoid concurrent modification
            for (Movie m : List.copyOf(actor.getMovies())) {
                m.getActors().remove(actor); // Remove the link
                movieRepository.save(m); // Persist change in the join table.
            }
        }

        actorRepository.delete(actor);
    }

    @Transactional(readOnly = true)
    public List<Actor> findByNameContainingIgnoreCase(String name) {
        return actorRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public Page<Actor> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return actorRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    @Transactional(readOnly = true)
    public List<Movie> getMoviesForActor(Long actorId) {
        return actorRepository.findById(actorId)
                .map(actor -> actor.getMovies().stream().toList())
                .orElse(List.of()); // Empty if actor not found
    }

    @Transactional
    public Optional<Actor> update(Long id, Actor updatedActor) {
        return actorRepository.findById(id)
        .map(actor -> {
            // Update only if non-null
            if (updatedActor.getName() != null && !updatedActor.getName().isBlank()) {
                actor.setName(updatedActor.getName());
            }
            if (updatedActor.getBirthDate() != null) {
                actor.setBirthDate(updatedActor.getBirthDate());
            }
            if (updatedActor.getMovies() != null) {
                actor.setMovies(updatedActor.getMovies());
            }
            return actorRepository.save(actor);
        });
    }
}
