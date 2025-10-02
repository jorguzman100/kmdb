package tech.kood.kmdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.kmdb.model.Genre;

// Inherits CRUD helpers from JpaRepository to interact with the DB
public interface GenreRepository extends JpaRepository<Genre, Long> {
    
    boolean existsByNameIgnoreCase(String name);
}
