package tech.kood.kmdb.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.kmdb.model.Actor;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    
    List<Actor> findByNameContainingIgnoreCase(String name);
    Page<Actor> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Helper for GET /api/movies/{movieId}/actors - Avoids Lazy
    List<Actor> findByMovies_Id(Long movieId);
}
