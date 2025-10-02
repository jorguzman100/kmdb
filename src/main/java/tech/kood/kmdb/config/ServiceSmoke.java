package tech.kood.kmdb.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import tech.kood.kmdb.repository.ActorRepository;
import tech.kood.kmdb.repository.GenreRepository;
import tech.kood.kmdb.repository.MovieRepository;

// Prints repositories counts after the seeding. Shows the DB is ready.
@Configuration
@Order(2)
public class ServiceSmoke {

  @Bean
  CommandLineRunner verifyServices(
      GenreRepository genreRepo,
      ActorRepository actorRepo,
      MovieRepository movieRepo
  ) {
    return args -> {
      System.out.println(">>> SERVICE SMOKE: start");

      System.out.println("genres.count = " + genreRepo.count());
      System.out.println("actors.count = " + actorRepo.count());
      System.out.println("movies.count = " + movieRepo.count());

      System.out.println(">>> SERVICE SMOKE: OK");
    };
  }
}
