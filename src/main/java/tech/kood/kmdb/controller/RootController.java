
package tech.kood.kmdb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
            "app", "Movie Database API",
            "status", "OK",
            "apiBase", "/api",
            "try", "/swagger-ui.html"
        );
    }

    @GetMapping("/api")
    public Map<String, Object> apiRoot() {
        return Map.of(
            "message", "Movie Database API — you’re at the API root.",
            "endpoints", "/api/movies, /api/genres, /api/actors",
            "docs", "/swagger-ui.html"
        );
    }
}


