package tech.kood.kmdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.kood.kmdb.model.Actor;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    
}
