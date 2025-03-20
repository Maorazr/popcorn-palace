package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.MovieDto;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.exception.MovieAlreadyExistsException;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {

  private final MovieRepository movieRepository;

  public List<MovieDto> getAllMovies() {
    return movieRepository.findAll().stream().map(this::entityToDto).collect(Collectors.toList());
  }

  public MovieDto createMovie(MovieDto movieDto) {
    if (movieRepository.findByTitle(movieDto.getTitle()).isPresent()) {
      throw new MovieAlreadyExistsException(
          "Movie titled " + movieDto.getTitle() + " already exists");
    }

    Movie movie = dtoToEntity(movieDto);
    Movie saved = movieRepository.save(movie);
    return entityToDto(saved);
  }

  public void updateMovie(String title, MovieDto movieDto) {
    Movie existing =
        movieRepository
            .findByTitle(title)
            .orElseThrow(
                () -> new ResourceNotFoundException("The movie " + title + " doesn't exists"));

    existing.setGenre(movieDto.getGenre());
    existing.setDuration(movieDto.getDuration());
    existing.setRating(movieDto.getRating());
    existing.setReleaseYear(movieDto.getReleaseYear());

    Movie updated = movieRepository.save(existing);
  }

  public void deleteMovie(String title) {
    Movie existing =
        movieRepository
            .findByTitle(title)
            .orElseThrow(
                () -> new ResourceNotFoundException("The movie " + title + " doesn't exists"));
    movieRepository.delete(existing);
  }

  private Movie dtoToEntity(MovieDto dto) {
    return Movie.builder()
        .title(dto.getTitle())
        .genre(dto.getGenre())
        .duration(dto.getDuration())
        .rating(dto.getRating())
        .releaseYear(dto.getReleaseYear())
        .build();
  }

  private MovieDto entityToDto(Movie movie) {
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
