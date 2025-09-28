package tech.kood.kmdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.kmdb.dto.MoviePatchDTO;
import tech.kood.kmdb.exception.ResourceNotFoundException;
import tech.kood.kmdb.model.Actor;
import tech.kood.kmdb.model.Genre;
import tech.kood.kmdb.model.Movie;
import tech.kood.kmdb.repository.MovieRepository;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // WRITE

    @Transactional
    public Movie create(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional
    public void delete(Long id) {
        movieRepository.deleteById(id);
    }

    // Force delete
    @Transactional
    public void delete(Long id, boolean force) {
        Movie movie = movieRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Movie " + id + " not found"));

        int related = movie.getGenres().size() + movie.getActors().size();
        if (!force && related > 0) {
            throw new IllegalArgumentException("Cannot delete movie '" + movie.getTitle() + "' because it has " + related + " associated genres/actors."); // 400 The GlobalException handler maps IllegalArgumentException
        }

        if (force) { // Detach from both sides and inerse sets
            for (Genre g : List.copyOf(movie.getGenres())) {
                movie.getGenres().remove(g); // Remove the link
            }
            for (Actor a : List.copyOf(movie.getActors())) {
                movie.getActors().remove(a); // Remove the link
            }
            movieRepository.save(movie); // persist join table cleanning
        }

        movieRepository.delete(movie);
    }

    @Transactional
    public Optional<Movie> update(Long id, Movie updatemovie) {
        return movieRepository.findById(id)
        .map(movie -> {
            if (updatemovie.getTitle() != null && !updatemovie.getTitle().isBlank()) {
                movie.setTitle(updatemovie.getTitle());
            }
            if (updatemovie.getReleaseYear() != 0) {
                movie.setReleaseYear(updatemovie.getReleaseYear());
            }
            if (updatemovie.getDuration() != 0) {
                movie.setDuration(updatemovie.getDuration());
            }
            if (updatemovie.getGenres() != null) {
                movie.setGenres(updatemovie.getGenres());
            }
            if (updatemovie.getActors() != null) {
                movie.setActors(updatemovie.getActors());
            }
            return movieRepository.save(movie);
        });
    }

    @Transactional
    public Optional<Movie> update(Long id, MoviePatchDTO patch) {
        return movieRepository.findById(id)
        .map(movie -> {
            if (patch.getTitle() != null) {
                movie.setTitle(patch.getTitle());
            }
            if (patch.getReleaseYear() != null) {
                movie.setReleaseYear(patch.getReleaseYear());
            }
            if (patch.getDuration() != null) {
                movie.setDuration(patch.getDuration());
            }
            return movieRepository.save(movie);
        });
    }

    // READ

    @Transactional(readOnly = true)
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Movie> findbyId(Long id) { 
        return movieRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Movie> findByGenre(Long genreId) {
        return movieRepository.findByGenres_Id(genreId);
    }

    @Transactional(readOnly = true)
    public List<Movie> findByYear(int releaseYear) {
        return movieRepository.findByReleaseYear(releaseYear);
    }

    @Transactional(readOnly = true)
    public List<Movie> findByActor(Long actorId) {
        return movieRepository.findByActors_Id(actorId);
    }
   
    @Transactional(readOnly = true)
    public List<Actor> getActorsForMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .map(movie -> movie.getActors().stream().toList())
                .orElse(List.of()); 
    }

    // READ - PAGED

    @Transactional(readOnly = true)
    public Page<Movie> findAll(Pageable pageable) {
        return movieRepository.findAll(pageable); // JpaRepository already provides findAll(Pageable) -> No need to declare in repo
    }

    @Transactional(readOnly = true)
    public Page<Movie> findByGenre(Long genreId, Pageable pageable) {
        return movieRepository.findByGenres_Id(genreId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Movie> findByYear(int releaseYear, Pageable pageable) {
        return movieRepository.findByReleaseYear(releaseYear, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Movie> findByActor(Long actorId, Pageable pageable) {
        return movieRepository.findByActors_Id(actorId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Movie> searchByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
    }
}
