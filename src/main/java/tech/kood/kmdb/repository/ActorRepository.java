package tech.kood.kmdb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.kmdb.model.Actor;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    
    List<Actor> findByNameContainingIgnoreCase(String name);

    // Helper for GET /api/movies/{movieId}/actors - Avoids Lazy
    List<Actor> findByMovies_Id(Long movieId);
}
