package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.BookingDto;
import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import com.att.tdp.popcorn_palace.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // POST /bookings
    @PostMapping
    public ResponseEntity<Map<String, UUID>> addBooking(@Valid @RequestBody BookingDto bookingDto) {
        UUID bookingId = bookingService.createBooking(bookingDto);
        return ResponseEntity.ok(Map.of("id", bookingId));
    }
}
