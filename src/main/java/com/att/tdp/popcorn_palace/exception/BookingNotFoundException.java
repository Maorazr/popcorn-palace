package com.att.tdp.popcorn_palace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingNotFoundException extends RuntimeException {
  public BookingNotFoundException(Long id) {
    super("Booking not found with ID = " + id);
  }
}

