package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.MovieDto;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

  private final MovieService movieService;

  // GET /movies/all
  @GetMapping("/all")
  public ResponseEntity<List<MovieDto>> getAllMovies() {
    List<MovieDto> movies = movieService.getAllMovies();
    return ResponseEntity.ok(movies);
  }

  // POST /movies
  @PostMapping
  public ResponseEntity<MovieDto> addMovie(@Valid @RequestBody MovieDto movieDto) {
    MovieDto created = movieService.createMovie(movieDto);
    return ResponseEntity.ok(created);
  }

  // POST /movies/update/{movieTitle}
  @PostMapping("/update/{movieTitle}")
  public ResponseEntity<Void> updateMovie(
      @PathVariable String movieTitle, @Valid @RequestBody MovieDto updatedMovieDto) {
    movieService.updateMovie(movieTitle, updatedMovieDto);
    return ResponseEntity.ok().build();
  }

  // DELETE /movie/{movieTitle}
  @DeleteMapping("/{movieTitle}")
  public ResponseEntity<Void> deleteMovie(@PathVariable String movieTitle) {
    movieService.deleteMovie(movieTitle);
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
