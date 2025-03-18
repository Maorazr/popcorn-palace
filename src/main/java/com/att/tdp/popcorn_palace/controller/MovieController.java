package com.att.tdp.popcorn_palace.controller;


import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // GET /movies/all
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieRepository.findAll());
    }

    // POST /movies
    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieRepository.save(movie);
        return ResponseEntity.ok(savedMovie);
    }

    // POST /movies/update/{movieTitle}
    @PostMapping("/update/{movieTitle}")
    public ResponseEntity<Void> updateMovie(@PathVariable String movieTitle, @RequestBody Movie updatedMovie) {
        Optional<Movie> optionalMovie = movieRepository.findByTitle(movieTitle);
        if (optionalMovie.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Movie exsitingMovie = optionalMovie.get();

        exsitingMovie.setGenre((updatedMovie.getGenre()));
        exsitingMovie.setDuration(updatedMovie.getDuration());
        exsitingMovie.setRating(updatedMovie.getRating());
        exsitingMovie.setReleaseYear(updatedMovie.getReleaseYear());

        movieRepository.save(exsitingMovie);
        return ResponseEntity.ok().build();
    }

    // DELETE /movie/{movieTitle}
    @DeleteMapping("/{movieTitle}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String movieTitle) {
        Optional<Movie> optionalMovie = movieRepository.findByTitle(movieTitle);
        if (optionalMovie.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        movieRepository.delete(optionalMovie.get());
        return ResponseEntity.ok().build();
    }
}

