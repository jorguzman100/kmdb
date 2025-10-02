package tech.kood.kmdb.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.kmdb.model.Movie;

// Inherits CRUD helpers from JpaRepository to interact with the DB
// Filtering queries for genre/year/actor
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByGenres_Id(Long genreId);

    List<Movie> findByReleaseYear(int releaseYear);

    List<Movie> findByActors_Id(Long actorId);

    // Paging
    Page<Movie> findByGenres_Id(Long genreId, Pageable pageable);
    Page<Movie> findByReleaseYear(int releaseYear, Pageable pageable);
    Page<Movie> findByActors_Id(Long actorId, Pageable pageable);

    // The title search
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
