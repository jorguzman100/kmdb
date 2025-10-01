package tech.kood.kmdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tech.kood.kmdb.exception.DuplicateResourceException;
import tech.kood.kmdb.exception.ResourceNotFoundException;
import tech.kood.kmdb.model.Genre;
import tech.kood.kmdb.model.Movie;
import tech.kood.kmdb.repository.GenreRepository;
import tech.kood.kmdb.repository.MovieRepository;

@Service
public class GenreService {
    
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository; // To update owning side on force delete

    // Spring will inject the repository
    public GenreService(GenreRepository genreRepository, MovieRepository movieRepository) {
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
    }

    // CRUD

    @Transactional
    public Genre create(Genre genre) {

        // Bonus: Ensures creating genre duplicates will not be allowed
        String name = genre.getName();
        String trimmed = name.trim();
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Genre name must not be empty.");
        }
        if (genreRepository.existsByNameIgnoreCase(trimmed)) {
            throw new DuplicateResourceException("Genre '" + trimmed + "' already exists.");
        }
        genre.setName(trimmed); // Normalize

        return genreRepository.save(genre);
    }
    
    @Transactional(readOnly = true)
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Genre> findAll(Pageable pageable) {
        return genreRepository.findAll(pageable); 
    }

    @Transactional(readOnly = true)
    public Optional<Genre> findbyId(Long id) { // Return Optional instead of null if not found
        return genreRepository.findById(id);
    }

    @Transactional
    public Optional<Genre> updateName(Long id, String newName) {
        return genreRepository.findById(id).map(genre -> {

            // Bonus: Ensures creating genre duplicates will not be allowed
            if (newName == null || newName.trim().isEmpty()) {
                throw new IllegalArgumentException("Genre name must not be empty");
            }
            String trimmed = newName.trim();
            if (!trimmed.equalsIgnoreCase(genre.getName()) && genreRepository.existsByNameIgnoreCase(trimmed)) {
                throw new DuplicateResourceException("Genre '" + trimmed + "' already exists.");
            }

            genre.setName(newName);
            return genreRepository.save(genre);
        });
    }

    @Transactional
    public void delete(Long id) {
        genreRepository.deleteById(id);
    }

    // Force delete
    @Transactional
    public void delete(Long id, boolean force) {
        Genre genre = genreRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Genre " + id + " not found"));

        int related = genre.getMovies().size();
        if (!force && related > 0) {
            String name = genre.getName();
            throw new IllegalArgumentException("Cannot delete genre '" + name + "' because it has " + related + " associated movies."); // 400 The GlobalException handler maps IllegalArgumentException
        }

        if (force) { // Detach from owning side and make a copy to avoid concurrent modification
            for (Movie m : List.copyOf(genre.getMovies())) {
                m.getGenres().remove(genre); // Remove the link
                movieRepository.save(m); // Persist change in the join table.
            }
        }

        genreRepository.delete(genre);
    }

    @Transactional(readOnly = true)
    public List<Movie> findMoviesByGenre(Long genreId) {
        return genreRepository.findById(genreId)
        .map(genre -> genre.getMovies().stream().toList())
        .orElse(List.of()); // Empty list if genre not found
    }
}
