package tech.kood.kmdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.kmdb.model.Actor;
import tech.kood.kmdb.model.Movie;
import tech.kood.kmdb.repository.MovieRepository;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // CRUD

    @Transactional
    public Movie create(Movie movie) {
        return movieRepository.save(movie);
    }

    @Transactional(readOnly = true)
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Movie> findbyId(Long id) { 
        return movieRepository.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        movieRepository.deleteById(id);
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
}
