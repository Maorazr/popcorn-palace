package com.att.tdp.popcorn_palace.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.eq;

import com.att.tdp.popcorn_palace.dto.BookingDto;
import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.exception.ShowtimeNotFoundException;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

class BookingServiceTest {

  AutoCloseable mocks;

  @Mock
  private BookingRepository bookingRepository;

  @Mock
  private ShowtimeRepository showtimeRepository;

  @InjectMocks
  private BookingService bookingService;

  @BeforeEach
  void setUp() {
    mocks = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    mocks.close();
  }

  @Test
  void testAddBooking_Succeeds() {
    long customShowtimeId = 123L;
    int customSeatNumber = 10;
    UUID customUserId = UUID.randomUUID();

    BookingDto dto = BookingDto.builder()
        .showtimeId(customShowtimeId)
        .seatNumber(customSeatNumber)
        .userId(customUserId)
        .build();

    Showtime showtime = Showtime.builder()
        .id(customShowtimeId)
        .movie(Movie.builder().title("Test Movie").build())
        .theater("Test Theater")
        .startTime(LocalDateTime.now().plusDays(1))
        .endTime(LocalDateTime.now().plusDays(1).plusHours(2))
        .price(10.0)
        .build();

    UUID savedId = UUID.randomUUID();
    Booking savedBooking = Booking.builder()
        .id(savedId)
        .showtime(showtime)
        .seatNumber(customSeatNumber)
        .userId(customUserId)
        .build();

    when(showtimeRepository.findById(eq(customShowtimeId)))
        .thenReturn(Optional.of(showtime));
    when(bookingRepository.existsByShowtimeIdAndSeatNumber(customShowtimeId, customSeatNumber))
        .thenReturn(false);
    when(bookingRepository.save(any(Booking.class)))
        .thenReturn(savedBooking);


    UUID result = bookingService.addBooking(dto);

    assertNotNull(result, "Expected a non-null booking ID");

    verify(showtimeRepository).findById(customShowtimeId);
    verify(bookingRepository).existsByShowtimeIdAndSeatNumber(customShowtimeId, customSeatNumber);
    verify(bookingRepository).save(any(Booking.class));
  }

  @Test
  void testAddBooking_SeatAlreadyBooked() {
    // ARRANGE
    long customShowtimeId = 111L;
    int customSeatNumber = 9;
    UUID customUserId = UUID.randomUUID();

    BookingDto dto = BookingDto.builder()
        .showtimeId(customShowtimeId)
        .seatNumber(customSeatNumber)
        .userId(customUserId)
        .build();

    Showtime showtime = Showtime.builder().id(customShowtimeId).build();
    when(showtimeRepository.findById(eq(customShowtimeId)))
        .thenReturn(Optional.of(showtime));

    when(bookingRepository.existsByShowtimeIdAndSeatNumber(customShowtimeId, customSeatNumber))
        .thenReturn(true);

    assertThrows(
        com.att.tdp.popcorn_palace.exception.SeatAlreadyBookedException.class,
        () -> bookingService.addBooking(dto),
        "Expected SeatAlreadyBookedException if the seat is already taken."
    );

    verify(bookingRepository, never()).save(any(Booking.class));
  }

  @Test
  void testAddBooking_ShowtimeNotFound() {

    long nonExistentShowtimeId = 9999L;
    int customSeatNumber = 5;
    UUID customUserId = UUID.randomUUID();

    BookingDto dto = BookingDto.builder()
        .showtimeId(nonExistentShowtimeId)
        .seatNumber(customSeatNumber)
        .userId(customUserId)
        .build();


    when(showtimeRepository.findById(eq(nonExistentShowtimeId)))
        .thenReturn(Optional.empty());


    assertThrows(
        ShowtimeNotFoundException.class,
        () -> bookingService.addBooking(dto),
        "Expected ShowtimeNotFoundException if the showtime doesn't exist."
    );

    verify(bookingRepository, never()).save(any(Booking.class));
  }
}
