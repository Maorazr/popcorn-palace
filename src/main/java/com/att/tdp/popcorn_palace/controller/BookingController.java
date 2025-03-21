package com.att.tdp.popcorn_palace.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.att.tdp.popcorn_palace.service.BookingService;
import com.att.tdp.popcorn_palace.dto.BookingDto;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

  private final BookingService bookingService;

  @PostMapping
  public ResponseEntity<Map<String, UUID>> addBooking(@Valid @RequestBody BookingDto bookingDto) {
    UUID bookingId = bookingService.addBooking(bookingDto);
    return ResponseEntity.ok(Map.of("id", bookingId));
  }
}
