package tech.kood.kmdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KmdbApplication {

	// Launches the app and wires the layers: Controllers, Services, Repositories
	public static void main(String[] args) {
		SpringApplication.run(KmdbApplication.class, args);
	}

}
