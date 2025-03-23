package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.MovieDto;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.exception.MovieAlreadyExistsException;
import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {


  @Mock private MovieRepository movieRepository;

  @InjectMocks private MovieService movieService;

  private Movie sampleMovie;
  private MovieDto sampleDto;

  @BeforeEach
  void setUp() {
    sampleMovie =
        Movie.builder()
            .id(1L)
            .title("Inception")
            .genre("Sci-Fi")
            .duration(148)
            .rating(8.8)
            .releaseYear(2010)
            .build();

    sampleDto =
        MovieDto.builder()
            .id(1L)
            .title("Inception")
            .genre("Sci-Fi")
            .duration(148)
            .rating(8.8)
            .releaseYear(2010)
            .build();
  }

  @Test
  @DisplayName("get all movies returns a list of movies")
  void testGetAllMovies() {
    Movie anotherMovie =
        Movie.builder()
            .id(2L)
            .title("The Matrix")
            .genre("Sci-Fi")
            .duration(136)
            .rating(8.7)
            .releaseYear(1999)
            .build();

    when(movieRepository.findAll()).thenReturn(asList(sampleMovie, anotherMovie));

    List<MovieDto> result = movieService.getAllMovies();

    assertEquals(2, result.size(), "Should return 2 movies");
    assertEquals("Inception", result.get(0).getTitle());
    assertEquals("The Matrix", result.get(1).getTitle());

    verify(movieRepository).findAll();
  }

  @Test
  @DisplayName("adds a movie successfully")
  void testAddMovie_Success() {
    when(movieRepository.findByTitle("Inception")).thenReturn(Optional.empty());

    when(movieRepository.save(any(Movie.class))).thenReturn(sampleMovie);

    MovieDto result = movieService.addMovie(sampleDto);

    assertNotNull(result, "Returned MovieDto should not be null");
    assertEquals("Inception", result.getTitle());

    verify(movieRepository).findByTitle("Inception");
    verify(movieRepository).save(any(Movie.class));
  }

  @Test
  @DisplayName("fails to add a movie throws MovieAlreadyExistsException")
  void testAddMovie_AlreadyExists() {

    when(movieRepository.findByTitle("Inception")).thenReturn(Optional.of(sampleMovie));

    assertThrows(
        MovieAlreadyExistsException.class,
        () -> movieService.addMovie(sampleDto),
        "Expect MovieAlreadyExistsException if the title is found");

    verify(movieRepository).findByTitle("Inception");
    verify(movieRepository, never()).save(any(Movie.class));
  }

  @Test
  @DisplayName("update a movie successfully")
  void testUpdateMovie_Success() {
    when(movieRepository.findByTitle("Inception")).thenReturn(Optional.of(sampleMovie));

    MovieDto updateDto =
        MovieDto.builder()
            .title("Inception")
            .genre("Action")
            .duration(149)
            .rating(9.0)
            .releaseYear(2010)
            .build();

    movieService.updateMovie("Inception", updateDto);

    verify(movieRepository).findByTitle("Inception");
    verify(movieRepository).save(any(Movie.class));
  }

  @Test
  @DisplayName("fails to update a movie throws MovieNotFoundException")
  void testUpdateMovie_NotFound() {
    when(movieRepository.findByTitle("Unknown")).thenReturn(Optional.empty());

    MovieDto updateDto = MovieDto.builder().title("Unknown").build();

    assertThrows(
        MovieNotFoundException.class,
        () -> movieService.updateMovie("Unknown", updateDto),
        "Updating a non-existing title should throw MovieNotFoundException");

    verify(movieRepository).findByTitle("Unknown");
    verify(movieRepository, never()).save(any(Movie.class));
  }

  @Test
  @DisplayName("delete a movie successfully")
  void testDeleteMovie_Success() {
    when(movieRepository.findByTitle("Inception")).thenReturn(Optional.of(sampleMovie));

    movieService.deleteMovie("Inception");

    verify(movieRepository).findByTitle("Inception");
    verify(movieRepository).delete(sampleMovie);
  }

  @Test
  @DisplayName("fails to delete a movie throws MovieNotFoundException")
  void testDeleteMovie_NotFound() {
    when(movieRepository.findByTitle("Unknown")).thenReturn(Optional.empty());

    assertThrows(MovieNotFoundException.class, () -> movieService.deleteMovie("Unknown"));

    verify(movieRepository).findByTitle("Unknown");
    verify(movieRepository, never()).delete(any(Movie.class));
  }
}
