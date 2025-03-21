package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.exception.OverlappingShowtimeException;
import com.att.tdp.popcorn_palace.exception.ShowtimeNotFoundException;
import com.att.tdp.popcorn_palace.exception.BadRequestException;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import com.att.tdp.popcorn_palace.dto.ShowtimeDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

class ShowtimeControllerTest {

  private AutoCloseable mocks;

  @Mock
  private ShowtimeService service;

  @InjectMocks
  private ShowtimeController controller;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  @DisplayName("getShowtimeById() - success")
  void testGetShowtimeById_Success() {
    ShowtimeDto dto = ShowtimeDto.builder()
        .id(1L)
        .movieId(1L)
        .theater("Cinema 1")
        .startTime(LocalDateTime.of(2025, 5, 10, 20, 0))
        .endTime(LocalDateTime.of(2025, 5, 10, 22, 0))
        .price(12.50)
        .build();

    when(service.getShowtimeById(10L)).thenReturn(dto);

    ResponseEntity<ShowtimeDto> response = controller.getShowtimeById(10L);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1L, response.getBody().getId());

    verify(service, times(1)).getShowtimeById(10L);
  }

  @Test
  @DisplayName("getShowtimeById() - not found")
  void testGetShowtimeById_NotFound() {
    when(service.getShowtimeById(999L))
        .thenThrow(new ShowtimeNotFoundException(999L));

    ShowtimeNotFoundException ex = assertThrows(
        ShowtimeNotFoundException.class,
        () -> controller.getShowtimeById(999L)
    );
    assertEquals("Showtime not found with ID = 999", ex.getMessage());
    verify(service, times(1)).getShowtimeById(999L);
  }

  @Test
  @DisplayName("addShowtime() - success")
  void testAddShowtime_Success() {
    ShowtimeDto dto = ShowtimeDto.builder()
        .movieId(1L)
        .theater("Cinema 1")
        .startTime(LocalDateTime.of(2025, 5, 15, 18, 0))
        .endTime(LocalDateTime.of(2025, 5, 15, 20, 0))
        .price(10.0)
        .build();

    ShowtimeDto createdDto = ShowtimeDto.builder()
        .id(20L)
        .movieId(1L)
        .theater("Cinema 1")
        .startTime(LocalDateTime.of(2025, 5, 15, 18, 0))
        .endTime(LocalDateTime.of(2025, 5, 15, 20, 0))
        .price(10.0)
        .build();

    when(service.addShowtime(any(ShowtimeDto.class))).thenReturn(createdDto);

    ResponseEntity<ShowtimeDto> response = controller.addShowtime(dto);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(20L, response.getBody().getId());

    verify(service, times(1)).addShowtime(any(ShowtimeDto.class));
  }

  @Test
  @DisplayName("addShowtime() - overlapping")
  void testAddShowtime_Overlapping() {
    ShowtimeDto dto = ShowtimeDto.builder()
        .movieId(1L)
        .theater("Cinema 2")
        .startTime(LocalDateTime.of(2025, 8, 1, 18, 0))
        .endTime(LocalDateTime.of(2025, 8, 1, 20, 0))
        .price(11.0)
        .build();

    when(service.addShowtime(any(ShowtimeDto.class)))
        .thenThrow(new OverlappingShowtimeException("Cinema 2"));

    OverlappingShowtimeException ex = assertThrows(
        OverlappingShowtimeException.class,
        () -> controller.addShowtime(dto)
    );

    assertEquals(
        "Cannot create showtime; overlapping schedule in theater 'Cinema 2'.",
        ex.getMessage()
    );

    verify(service, times(1)).addShowtime(any(ShowtimeDto.class));
  }

  @Test
  @DisplayName("addShowtime() - bad request")
  void testAddShowtime_BadRequest() {
    ShowtimeDto dto = ShowtimeDto.builder()
        .movieId(null)
        .theater("")
        .startTime(LocalDateTime.of(2025, 8, 10, 20, 0))
        .endTime(LocalDateTime.of(2025, 8, 10, 22, 0))
        .price(-5.0)
        .build();

    when(service.addShowtime(any(ShowtimeDto.class)))
        .thenThrow(new BadRequestException("Invalid showtime data"));

    BadRequestException ex = assertThrows(
        BadRequestException.class,
        () -> controller.addShowtime(dto)
    );

    assertEquals("Bad Request: Invalid showtime data", ex.getMessage());

    verify(service, times(1)).addShowtime(any(ShowtimeDto.class));
  }

  @Test
  @DisplayName("updateShowtime() - success")
  void testUpdateShowtime_Success() {
    ShowtimeDto dto = ShowtimeDto.builder()
        .movieId(2L)
        .theater("Cinema 1")
        .startTime(LocalDateTime.of(2025, 8, 2, 17, 0))
        .endTime(LocalDateTime.of(2025, 8, 2, 19, 0))
        .price(12.50)
        .build();

    doNothing().when(service).updateShowtime(eq(30L), any(ShowtimeDto.class));

    ResponseEntity<Void> response = controller.updateShowtime(30L, dto);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    verify(service, times(1)).updateShowtime(eq(30L), any(ShowtimeDto.class));
  }

  @Test
  @DisplayName("updateShowtime() - not found")
  void testUpdateShowtime_NotFound() {
    ShowtimeDto dto = ShowtimeDto.builder().build();

    doThrow(new ShowtimeNotFoundException(999L))
        .when(service).updateShowtime(eq(999L), any(ShowtimeDto.class));

    ShowtimeNotFoundException ex = assertThrows(
        ShowtimeNotFoundException.class,
        () -> controller.updateShowtime(999L, dto)
    );

    assertEquals("Showtime not found with ID = 999", ex.getMessage());

    verify(service, times(1)).updateShowtime(eq(999L), any(ShowtimeDto.class));
  }

  @Test
  @DisplayName("deleteShowtime() - success")
  void testDeleteShowtime_Success() {
    doNothing().when(service).deleteShowtime(40L);

    ResponseEntity<Void> response = controller.deleteShowtime(40L);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    verify(service, times(1)).deleteShowtime(40L);
  }

  @Test
  @DisplayName("deleteShowtime() - not found")
  void testDeleteShowtime_NotFound() {
    doThrow(new ShowtimeNotFoundException(888L))
        .when(service).deleteShowtime(888L);
    ShowtimeNotFoundException ex = assertThrows(
        ShowtimeNotFoundException.class,
        () -> controller.deleteShowtime(888L)
    );
    assertEquals("Showtime not found with ID = 888", ex.getMessage());
    verify(service, times(1)).deleteShowtime(888L);
  }
}
