package com.att.tdp.popcorn_palace.controller;

import static org.mockito.Mockito.when;

import com.att.tdp.popcorn_palace.dto.MovieDto;
import com.att.tdp.popcorn_palace.exception.BadRequestException;
import com.att.tdp.popcorn_palace.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private MovieService movieService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {

  }

  @Test
  @DisplayName("Test fetching all movies from the controller")
  void testGetAllMovies() throws Exception {
    MovieDto testMovieDto = MovieDto.builder()
        .id(1L)
        .title("Sallah Shabati")
        .genre("Bourekas")
        .duration(110)
        .rating(7.2)
        .releaseYear(1964)
        .build();

    when(movieService.getAllMovies()).thenReturn(List.of(testMovieDto));

    mockMvc.perform(get("/movies/all"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()").value(1))
        .andExpect(jsonPath("$[0].title").value("Sallah Shabati"))
        .andExpect(jsonPath("$[0].genre").value("Bourekas"));


    verify(movieService, times(1)).getAllMovies();
  }

  @Test
  @DisplayName("Test adding a movie - success")
  void testAddMovie_Success() throws Exception {
    MovieDto requestDto = MovieDto.builder()
        .title("The Green Mile")
        .genre("Drama")
        .duration(189)
        .rating(8.6)
        .releaseYear(1999)
        .build();

    MovieDto responseDto = MovieDto.builder()
        .id(1L)
        .title("The Green Mile")
        .genre("Drama")
        .duration(189)
        .rating(8.6)
        .releaseYear(1999)
        .build();

    when(movieService.createMovie(any(MovieDto.class))).thenReturn(requestDto);

    mockMvc.perform(post("/movies")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(requestDto)))

        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("The Green Mile"))
        .andExpect(jsonPath("$.genre").value("Drama"))
        .andExpect(jsonPath("$.duration").value(189));

    verify(movieService, times(1)).createMovie(any(MovieDto.class));
  }

  @Test
  @DisplayName("Test adding a movie - failure")
  void testAddMovie_Failure() throws Exception {
    MovieDto invalidMovieDto = MovieDto.builder()
        .title("")
        .genre("Drama")
        .duration(189)
        .rating(8.6)
        .releaseYear(1999)
        .build();

    when(movieService.createMovie(any(MovieDto.class)))
        .thenThrow(new BadRequestException("Invalid movie title"));
  }
}
