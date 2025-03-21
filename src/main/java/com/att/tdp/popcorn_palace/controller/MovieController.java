package com.att.tdp.popcorn_palace.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import com.att.tdp.popcorn_palace.service.MovieService;
import org.springframework.http.ResponseEntity;
import com.att.tdp.popcorn_palace.dto.MovieDto;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

  private final MovieService movieService;

  @GetMapping("/all")
  public ResponseEntity<List<MovieDto>> getAllMovies() {
    List<MovieDto> movies = movieService.getAllMovies();
    return ResponseEntity.ok(movies);
  }

  @PostMapping
  public ResponseEntity<MovieDto> addMovie(@Valid @RequestBody MovieDto movieDto) {
    MovieDto created = movieService.createMovie(movieDto);
    return ResponseEntity.ok(created);
  }

  @PostMapping("/update/{movieTitle}")
  public ResponseEntity<Void> updateMovie(
      @PathVariable String movieTitle, @Valid @RequestBody MovieDto updatedMovieDto) {
    movieService.updateMovie(movieTitle, updatedMovieDto);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{movieTitle}")
  public ResponseEntity<Void> deleteMovie(@PathVariable String movieTitle) {
    movieService.deleteMovie(movieTitle);
    return ResponseEntity.ok().build();
  }
}
