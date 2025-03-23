package com.att.tdp.popcorn_palace.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.att.tdp.popcorn_palace.dto.ShowtimeDto;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;
import com.att.tdp.popcorn_palace.exception.OverlappingShowtimeException;
import com.att.tdp.popcorn_palace.exception.ShowtimeNotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShowtimeServiceTest {

  private AutoCloseable mocks;

  @Mock
  private ShowtimeRepository showtimeRepository;

  @Mock
  private MovieRepository movieRepository;

  @InjectMocks
  private ShowtimeService showtimeService;

  private final Long movieId = 1L;
  private final Long showtimeId = 10L;

  @Test
  @DisplayName("get showtime by id")
  void testGetShowtimeById_Success() {
    Showtime showtime = sampleShowtime();

    when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(showtime));

    ShowtimeDto dto = showtimeService.getShowtimeById(showtimeId);

    assertNotNull(dto);
    assertEquals("Cinema 1", dto.getTheater());
    verify(showtimeRepository).findById(showtimeId);
  }

  @Test
  @DisplayName("fails to get showtime by id, showtime not found")
  void testGetShowtimeById_NotFound() {
    when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.empty());

    assertThrows(ShowtimeNotFoundException.class, () -> showtimeService.getShowtimeById(showtimeId));
  }

  @Test
  @DisplayName("add showtime")
  void testAddShowtime_Success() {
    ShowtimeDto dto = sampleShowtimeDto();
    Movie movie = sampleMovie();
    Showtime saved = sampleShowtime();

    when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
    when(showtimeRepository.existsOverlappingShowtime(anyLong(), any(), any(), any())).thenReturn(false);
    when(showtimeRepository.save(any(Showtime.class))).thenReturn(saved);

    ShowtimeDto result = showtimeService.addShowtime(dto);

    assertNotNull(result);
    assertEquals(dto.getTheater(), result.getTheater());
    verify(showtimeRepository).save(any(Showtime.class));
  }

  @Test
  @DisplayName("fails to add showtime, movie not found")
  void testAddShowtime_MovieNotFound() {
    ShowtimeDto dto = sampleShowtimeDto();

    when(movieRepository.findById(movieId)).thenReturn(Optional.empty());
    assertThrows(MovieNotFoundException.class, () -> showtimeService.addShowtime(dto));
  }

  @Test
  @DisplayName("fails to add showtime, overlapping showtime")
  void testAddShowtime_Overlapping() {
    ShowtimeDto dto = sampleShowtimeDto();
    Movie movie = sampleMovie();

    when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
    when(showtimeRepository.existsOverlappingShowtime(anyLong(), any(), any(), any())).thenReturn(true);

    assertThrows(OverlappingShowtimeException.class, () -> showtimeService.addShowtime(dto));
  }

  @Test
  @DisplayName("update showtime")
  void testUpdateShowtime_Success() {
    ShowtimeDto dto = sampleShowtimeDto();
    Showtime existing = sampleShowtime();
    Movie movie = sampleMovie();

    when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(existing));
    when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
    when(showtimeRepository.save(any(Showtime.class))).thenReturn(existing);

    assertDoesNotThrow(() -> showtimeService.updateShowtime(showtimeId, dto));
    verify(showtimeRepository).save(any(Showtime.class));
  }

  @Test
  @DisplayName("fails to update showtime, showtime not found")
  void testUpdateShowtime_ShowtimeNotFound() {
    ShowtimeDto dto = sampleShowtimeDto();
    when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.empty());

    assertThrows(ShowtimeNotFoundException.class,
        () -> showtimeService.updateShowtime(showtimeId, dto));
  }


  @Test
  @DisplayName("fails to update showtime, movie not found")
  void testUpdateShowtime_MovieNotFound() {
    ShowtimeDto dto = sampleShowtimeDto();

    when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(sampleShowtime()));
    when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

    assertThrows(MovieNotFoundException.class, () -> showtimeService.updateShowtime(showtimeId, dto));
  }

  @Test
  @DisplayName("delete showtime")
  void testDeleteShowtime_Success() {
    Showtime existing = sampleShowtime();
    when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.of(existing));

    assertDoesNotThrow(() -> showtimeService.deleteShowtime(showtimeId));

    verify(showtimeRepository).delete(existing);
  }

  @Test
  @DisplayName("fails to delete showtime, showtime not found")
  void testDeleteShowtime_NotFound() {
    when(showtimeRepository.findById(showtimeId)).thenReturn(Optional.empty());

    assertThrows(ShowtimeNotFoundException.class,
        () -> showtimeService.deleteShowtime(showtimeId));
  }

  private Showtime sampleShowtime() {
    return Showtime.builder()
        .id(showtimeId)
        .movie(sampleMovie())
        .theater("Cinema 1")
        .startTime(LocalDateTime.of(2025, 5, 10, 20, 0))
        .endTime(LocalDateTime.of(2025, 5, 10, 22, 0))
        .price(15.0)
        .build();
  }

  private ShowtimeDto sampleShowtimeDto() {
    return ShowtimeDto.builder()
        .movieId(movieId)
        .theater("Cinema 1")
        .startTime(LocalDateTime.of(2025, 5, 10, 20, 0))
        .endTime(LocalDateTime.of(2025, 5, 10, 22, 0))
        .price(15.0)
        .build();
  }

  private Movie sampleMovie() {
    return Movie.builder()
        .id(movieId)
        .title("The Green Mile")
        .genre("Drama")
        .duration(189)
        .rating(8.6)
        .releaseYear(1999)
        .build();
  }
}
