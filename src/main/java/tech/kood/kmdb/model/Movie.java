package tech.kood.kmdb.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Movie {
    private @Id @GeneratedValue Long id;
    private String title;
    private int releaseYear;
    private int duration;

    // Relationships - Movie is the owner

    // Movie-Genre
    @ManyToMany
    private Set<Genre> genres = new HashSet<>();

    // Movie-Actor
    @ManyToMany
    private Set<Actor> actors = new HashSet<>();

    public Movie() {}
    public Movie(String title, int releaseYear, int duration) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    // Getters & Setters for relationships
    
    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
    
    public Set<Actor> getActors() {
        return actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }
}
