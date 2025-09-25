package tech.kood.kmdb.config;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import tech.kood.kmdb.model.Actor;
import tech.kood.kmdb.model.Genre;
import tech.kood.kmdb.model.Movie;
import tech.kood.kmdb.repository.ActorRepository;
import tech.kood.kmdb.repository.GenreRepository;
import tech.kood.kmdb.repository.MovieRepository;

@Configuration
@Order(1)
public class Seeder {

    @Bean
    CommandLineRunner seed(
            GenreRepository genreRepo,
            ActorRepository actorRepo,
            MovieRepository movieRepo) {
        return args -> {
            System.out.println(">>> SEEDER: starting");
            boolean hasMovies = false;
            try {
                hasMovies = movieRepo.count() > 0; // may fail if table not ready/locked
            } catch (Exception e) {
                System.out.println(">>> SEEDER: count() failed, continuing with seeding. Cause: " + e.getMessage());
            }
            if (hasMovies) {
                System.out.println(">>> SEEDER: movies already present, skipping seed.");
                return;
            }

            // Genres
            Map<String, Genre> genres = new HashMap<>();
            for (String genre : List.of("Action", "Sci-Fi", "Thriller", "Drama", "Comedy")) {
                genres.put(genre, genreRepo.save(new Genre(genre)));
            }

            // Actors
            String[][] ACTORS = {
                    { "Leonardo DiCaprio", "1974" },
                    { "Tom Hardy", "1977" },
                    { "Keanu Reeves", "1964" },
                    { "Carrie-Anne Moss", "1967" },
                    { "Laurence Fishburne", "1961" },
                    { "Christian Bale", "1974" },
                    { "Heath Ledger", "1979" },
                    { "Matthew McConaughey", "1969" },
                    { "Anne Hathaway", "1982" },
                    { "Brad Pitt", "1963" },
                    { "Morgan Freeman", "1937" },
                    { "Samuel L. Jackson", "1948" },
                    { "Robert Downey Jr.", "1965" },
                    { "Chris Evans", "1981" },
                    { "Scarlett Johansson", "1984" },
            };
            Map<String, Actor> actors = new HashMap<>();
            for (String[] row : ACTORS) {
                String name = row[0];
                int year = Integer.parseInt(row[1]);
                actors.put(name, actorRepo.save(new Actor(name, LocalDate.of(year, 1, 1))));
            }

            // Movies
            // Format: { title, releaseYear, "Genre1|Genre2|...", "Actor1;Actor2;..." }
            String[][] MOVIES = {
                    { "The Matrix", "1999", "Action|Sci-Fi", "Keanu Reeves;Carrie-Anne Moss;Laurence Fishburne" },
                    { "Inception", "2010", "Action|Sci-Fi|Thriller", "Leonardo DiCaprio;Tom Hardy" },
                    { "The Dark Knight", "2008", "Action|Drama|Thriller",
                            "Christian Bale;Heath Ledger;Morgan Freeman" },
                    { "Interstellar", "2014", "Sci-Fi|Drama", "Matthew McConaughey;Anne Hathaway" },
                    { "The Avengers", "2012", "Action|Sci-Fi", "Robert Downey Jr.;Chris Evans;Scarlett Johansson" },
                    { "Gladiator", "2000", "Action|Drama", "Russell Crowe;Joaquin Phoenix" },
                    { "Fight Club", "1999", "Drama|Thriller", "Brad Pitt" },
                    { "Pulp Fiction", "1994", "Drama|Comedy", "Samuel L. Jackson" },
                    { "The Shawshank Redemption", "1994", "Drama", "Morgan Freeman" },
                    { "Forrest Gump", "1994", "Drama", "Tom Hanks" },
                    { "The Fellowship of the Ring", "2001", "Action|Drama", "Elijah Wood;Ian McKellen" },
                    { "The Two Towers", "2002", "Action|Drama", "Elijah Wood;Ian McKellen" },
                    { "The Return of the King", "2003", "Action|Drama", "Elijah Wood;Ian McKellen" },
                    { "Mad Max: Fury Road", "2015", "Action|Sci-Fi", "Tom Hardy" },
                    { "Avatar", "2009", "Sci-Fi|Action", "Sam Worthington;Zoe Saldana" },
                    { "Joker", "2019", "Drama", "Robert De Niro;Joaquin Phoenix" },
                    { "The Matrix Reloaded", "2003", "Action|Sci-Fi", "Keanu Reeves;Carrie-Anne Moss" },
                    { "The Matrix Revolutions", "2003", "Action|Sci-Fi", "Keanu Reeves;Carrie-Anne Moss" },
                    { "The Prestige", "2006", "Drama|Thriller", "Christian Bale;Scarlett Johansson" },
                    // Ensure Comedy has at least 2: add one more with Comedy
                    { "The Wolf of Wall Street", "2013", "Drama|Comedy", "Leonardo DiCaprio" }
            };
            for (String[] row : MOVIES) {
                String title = row[0];
                int year = Integer.parseInt(row[1]);
                String[] gNames = row[2].split("\\|");
                String[] aNames = row[3].split("\\;");
                Movie m = new Movie(title, year, 120); // All durations to 120

                // Link genres
                for (String gName : gNames) {
                    Genre g = genres.get(gName.trim());
                    if (g != null)
                        m.getGenres().add(g);
                }

                // Link actors (or create if missing)
                for (String aNameRow : aNames) {
                    String aName = aNameRow.trim();
                    Actor a = actors.get(aName);
                    if (a == null) { // Actor missing -> Create with 1970 default birthDate
                        a = actorRepo.save(new Actor(aName, LocalDate.of(1970, 1, 1)));
                        actors.put(aName, a);
                    }
                    m.getActors().add(a);
                }
                movieRepo.save(m); // Writes join rows - Movie is the owner of ManyToMany
            }
            // add at the very end of your seed(...) lambda, after saving all movies
            long g = genreRepo.count();
            long a = actorRepo.count();
            long m = movieRepo.count();
            var years = movieRepo.findAll().stream().map(Movie::getReleaseYear).toList();
            int minYear = years.stream().min(Integer::compareTo).orElse(0);
            int maxYear = years.stream().max(Integer::compareTo).orElse(0);

            System.out.println("=== SEED CHECK ===");
            System.out.println("Genres: " + g + " (>=5)");
            System.out.println("Actors: " + a + " (>=15)");
            System.out.println("Movies: " + m + " (>=20)");
            System.out.println("Years span: " + minYear + " .. " + maxYear + " (>= 2 decades)");
            System.out.println("==================");
        };
    }

}
