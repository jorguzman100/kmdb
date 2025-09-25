package tech.kood.kmdb.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import tech.kood.kmdb.repository.ActorRepository;
import tech.kood.kmdb.repository.GenreRepository;
import tech.kood.kmdb.repository.MovieRepository;

@Configuration
@Order(2) // <= ensure this runs AFTER the seeder
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

      // IMPORTANT: do not traverse lazy relationships here.
      // Keep the smoke minimal so startup never hangs.

      System.out.println(">>> SERVICE SMOKE: OK");
    };
  }
}
