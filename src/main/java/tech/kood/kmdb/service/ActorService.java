package tech.kood.kmdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.kmdb.model.Actor;
import tech.kood.kmdb.model.Movie;
import tech.kood.kmdb.repository.ActorRepository;

@Service
public class ActorService {

    private final ActorRepository actorRepository;

    // Spring will inject the repository
    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
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
    public Optional<Actor> findbyId(Long id) { // Return Optional instead of null if not found
        return actorRepository.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        actorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Actor> findByNameContainingIgnoreCase(String name) {
        return actorRepository.findByNameContainingIgnoreCase(name);
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
