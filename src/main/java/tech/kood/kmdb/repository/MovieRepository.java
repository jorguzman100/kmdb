package tech.kood.kmdb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.kmdb.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByGenres_Id(Long genreId);

    List<Movie> findByReleaseYear(int releaseYear);

    // Optional
    List<Movie> findByActors_Id(Long actorId);

    // Extra if implementing the optional title search
    // List<Movie> findByTitleContainingIgnoreCase(String title);
}
