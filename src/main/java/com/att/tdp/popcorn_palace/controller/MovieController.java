package com.att.tdp.popcorn_palace.controller;


import com.att.tdp.popcorn_palace.dto.MovieDto;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieRepository movieRepository;

//    public MovieController(MovieRepository movieRepository) {
//        this.movieRepository = movieRepository;
//    }

    // GET /movies/all
    @GetMapping("/all")
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        List<MovieDto> movies = movieRepository.findAll().stream()
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(movies);
    }

    // POST /movies
    @PostMapping
    public ResponseEntity<MovieDto> addMovie(@Valid @RequestBody MovieDto movieDto) {
        Movie movie = Movie.builder()
                .title(movieDto.getTitle())
                .genre(movieDto.getGenre())
                .duration(movieDto.getDuration())
                .releaseYear(movieDto.getReleaseYear())
                .build();

        Movie savedMovie = movieRepository.save(movie);
        return ResponseEntity.ok(toDto(savedMovie));
    }

    // POST /movies/update/{movieTitle}
    @PostMapping("/update/{movieTitle}")
    public ResponseEntity<Void> updateMovie(@PathVariable String movieTitle,@Valid @RequestBody MovieDto updatedMovieDto) {
        Optional<Movie> optionalMovie = movieRepository.findByTitle(movieTitle);
        if (optionalMovie.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Movie exsitingMovie = optionalMovie.get();

        exsitingMovie.setGenre((updatedMovieDto.getGenre()));
        exsitingMovie.setDuration(updatedMovieDto.getDuration());
        exsitingMovie.setRating(updatedMovieDto.getRating());
        exsitingMovie.setReleaseYear(updatedMovieDto.getReleaseYear());

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

    private MovieDto toDto(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .genre(movie.getGenre())
                .duration(movie.getDuration())
                .rating(movie.getRating())
                .releaseYear(movie.getReleaseYear())
                .build();
    }
}

