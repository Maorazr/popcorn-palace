package com.att.tdp.popcorn_palace.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import com.att.tdp.popcorn_palace.dto.MovieDto;
import com.att.tdp.popcorn_palace.exception.BadRequestException;
import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;
import com.att.tdp.popcorn_palace.service.MovieService;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

public class MovieControllerTest {

  @Mock private MovieService movieService;

  @InjectMocks private MovieController movieController;

  private AutoCloseable mocks;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  @DisplayName("get all movies and return status 200 OK")
  void testGetAllMovies() {
    MovieDto sampleMovie =
        MovieDto.builder()
            .id(1L)
            .title("Sallah Shabati")
            .genre("Bourekas")
            .duration(110)
            .rating(7.2)
            .releaseYear(1964)
            .build();

    when(movieService.getAllMovies()).thenReturn(List.of(sampleMovie));

    ResponseEntity<List<MovieDto>> response = movieController.getAllMovies();

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Sallah Shabati", response.getBody().getFirst().getTitle());

    verify(movieService, times(1)).getAllMovies();
  }

  @Test
  @DisplayName("adds a movie and returns status 200 OK")
  void testAddMovie_Success() {
    MovieDto requestDto =
        MovieDto.builder()
            .title("The Green Mile")
            .genre("Drama")
            .duration(189)
            .rating(8.6)
            .releaseYear(1999)
            .build();

    MovieDto createdDto =
        MovieDto.builder()
            .id(1L)
            .title("The Green Mile")
            .genre("Drama")
            .duration(189)
            .rating(8.6)
            .releaseYear(1999)
            .build();

    when(movieService.addMovie(any(MovieDto.class))).thenReturn(createdDto);

    ResponseEntity<MovieDto> response = movieController.addMovie(requestDto);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("The Green Mile", response.getBody().getTitle());

    verify(movieService, times(1)).addMovie(any(MovieDto.class));
  }

  @Test
  @DisplayName("fails to add a movie throws BadRequestException")
  void testAddMovie_Failure() {
    MovieDto invalidDto =
        MovieDto.builder()
            .title("")
            .genre("Drama")
            .duration(189)
            .rating(8.6)
            .releaseYear(1999)
            .build();

    when(movieService.addMovie(any(MovieDto.class)))
        .thenThrow(new BadRequestException("Invalid movie title"));

    BadRequestException ex =
        assertThrows(BadRequestException.class, () -> movieController.addMovie(invalidDto));

    assertEquals("Bad Request: Invalid movie title", ex.getMessage());

    verify(movieService, times(1)).addMovie(any(MovieDto.class));
  }

  @Test
  @DisplayName("update a movie and return status 200 OK")
  void testUpdateMovie_Success() {
    MovieDto updatedDto =
        MovieDto.builder()
            .id(1L)
            .title("The Green Mile")
            .genre("Drama")
            .duration(189)
            .rating(8.6)
            .releaseYear(1999)
            .build();

    doNothing().when(movieService).updateMovie(eq("The Green Mile"), any(MovieDto.class));

    ResponseEntity<Void> response = movieController.updateMovie("The Green Mile", updatedDto);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    verify(movieService, times(1)).updateMovie(eq("The Green Mile"), any(MovieDto.class));
  }

  @Test
  @DisplayName("fails to update a movie throws MovieNotFoundException ")
  void testUpdateMovie_NotFound() {
    MovieDto updatedDto = MovieDto.builder().title("NonExistent").genre("Drama").build();

    doThrow(new MovieNotFoundException("Unknown"))
        .when(movieService)
        .updateMovie(eq("Unknown"), any(MovieDto.class));

    MovieNotFoundException ex =
        assertThrows(
            MovieNotFoundException.class, () -> movieController.updateMovie("Unknown", updatedDto));

    assertEquals("Movie not found with title = 'Unknown'", ex.getMessage());

    verify(movieService, times(1)).updateMovie(eq("Unknown"), any(MovieDto.class));
  }

  @Test
  @DisplayName("delete a movie and returns status 200 OK")
  void testDeleteMovie_Success() {
    doNothing().when(movieService).deleteMovie("The Green Mile");

    ResponseEntity<Void> response = movieController.deleteMovie("The Green Mile");

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    verify(movieService, times(1)).deleteMovie("The Green Mile");
  }

  @Test
  @DisplayName("fails to delete a movie throws MovieNotFoundException")
  void testDeleteMovie_NotFound() {
    doThrow(new MovieNotFoundException("Unknown")).when(movieService).deleteMovie("Unknown");

    MovieNotFoundException ex =
        assertThrows(MovieNotFoundException.class, () -> movieController.deleteMovie("Unknown"));

    assertEquals("Movie not found with title = 'Unknown'", ex.getMessage());

    verify(movieService, times(1)).deleteMovie("Unknown");
  }
}
