package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.dto.BookingDto;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

  private final BookingRepository bookingRepository;
  private final ShowtimeRepository showtimeRepository;

  public UUID createBooking(BookingDto bookingDto) {
    Showtime showtime =
        showtimeRepository
            .findById(bookingDto.getShowtimeId())
            .orElseThrow(() -> new RuntimeException("Showtime not found"));

    boolean seatExists =
        bookingRepository.existsByShowtimeIdAndSeatNumber(
            bookingDto.getShowtimeId(), bookingDto.getSeatNumber());

    if (seatExists) {
      throw new RuntimeException("Seat is already booked");
    }

    Booking booking =
        Booking.builder()
            .showtime(showtime)
            .userId(bookingDto.getUserId())
            .seatNumber(bookingDto.getSeatNumber())
            .build();

    Booking savedBooking = bookingRepository.save(booking);
    return savedBooking.getId();
  }
}
