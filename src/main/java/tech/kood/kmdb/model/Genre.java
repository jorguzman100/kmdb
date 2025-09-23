package tech.kood.kmdb.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Genre {
    private @Id @GeneratedValue Long id;
    private String name;

    // Relationships - inverse side
    
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies = new HashSet<>();

    public Genre() {}
    public Genre(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getters & Setters for relationships
    
    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }
}
