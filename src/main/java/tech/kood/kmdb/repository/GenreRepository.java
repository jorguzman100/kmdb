package tech.kood.kmdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.kmdb.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    
}
