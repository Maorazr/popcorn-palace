package com.att.tdp.popcorn_palace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class SeatAlreadyBookedException extends RuntimeException {
  public SeatAlreadyBookedException(Long showtimeId, int seatNumber) {
    super("Seat " + seatNumber + " is already booked for showtime with ID = " + showtimeId);
  }
}
