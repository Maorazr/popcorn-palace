package com.att.tdp.popcorn_palace.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.att.tdp.popcorn_palace.dto.BookingDto;
import com.att.tdp.popcorn_palace.exception.SeatAlreadyBookedException;
import com.att.tdp.popcorn_palace.service.BookingService;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BookingControllerTest {

  private AutoCloseable mocks;

  @Mock private BookingService bookingService;

  @InjectMocks private BookingController bookingController;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  @DisplayName("adds a booking and returns status 200 OK")
  void testAddBooking_Success() {
    BookingDto dto =
        BookingDto.builder()
            .showtimeId(1L)
            .seatNumber(1)
            .userId(UUID.fromString("00000000-0000-0000-0000-000000000000"))
            .build();

    UUID mockBookingId = UUID.fromString("AAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA");

    when(bookingService.addBooking(any(BookingDto.class))).thenReturn(mockBookingId);

    ResponseEntity<Map<String, UUID>> response = bookingController.addBooking(dto);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().containsKey("id"));
    assertEquals(mockBookingId, response.getBody().get("id"));

    verify(bookingService, times(1)).addBooking(any(BookingDto.class));
  }

  @Test
  @DisplayName("fails to add a booking due to a seat already being booked")
  void testAddBooking_SeatAlreadyBooked() {
    BookingDto requestDto =
        BookingDto.builder()
            .showtimeId(1L)
            .seatNumber(7)
            .userId(UUID.fromString("00000000-AAAA-BBBB-CCCC-DDDDDDDDDDDD"))
            .build();

    when(bookingService.addBooking(any(BookingDto.class)))
        .thenThrow(new SeatAlreadyBookedException(1L, 7));

    SeatAlreadyBookedException ex =
        assertThrows(
            SeatAlreadyBookedException.class, () -> bookingController.addBooking(requestDto));

    assertEquals("Seat 7 is already booked for showtime with ID = 1", ex.getMessage());

    verify(bookingService, times(1)).addBooking(any(BookingDto.class));
  }
}
