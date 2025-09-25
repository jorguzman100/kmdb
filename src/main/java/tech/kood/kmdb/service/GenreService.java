package tech.kood.kmdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.kmdb.model.Genre;
import tech.kood.kmdb.model.Movie;
import tech.kood.kmdb.repository.GenreRepository;

@Service
public class GenreService {
    
    private final GenreRepository genreRepository;

    // Spring will inject the repository
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    // CRUD

    @Transactional
    public Genre create(Genre genre) {
        return genreRepository.save(genre);
    }
    
    @Transactional(readOnly = true)
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Genre> findbyId(Long id) { // Return Optional instead of null if not found
        return genreRepository.findById(id);
    }

    @Transactional
    public Optional<Genre> updateName(Long id, String newName) {
        return genreRepository.findById(id).map(genre -> {
            genre.setName(newName);
            return genreRepository.save(genre);
        });
    }

    @Transactional
    public void delete(Long id) {
        genreRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Movie> findMoviesByGenre(Long genreId) {
        return genreRepository.findById(genreId)
        .map(genre -> genre.getMovies().stream().toList())
        .orElse(List.of()); // Empty list if genre not found
    }
}
