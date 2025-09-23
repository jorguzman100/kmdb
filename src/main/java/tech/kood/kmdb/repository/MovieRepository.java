package tech.kood.kmdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.kmdb.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

}
