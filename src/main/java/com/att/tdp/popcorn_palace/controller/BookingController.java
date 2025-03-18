package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.entity.Booking;
import com.att.tdp.popcorn_palace.repository.BookingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/bookings")

public class BookingController {

    private final BookingRepository bookingRepository;

    public BookingController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // POST /bookings
    @PostMapping
    public ResponseEntity<Map<String, UUID>> addBooking(@RequestBody Booking booking) {
        Booking savedBooking = bookingRepository.save(booking);
        return ResponseEntity.ok(Map.of("id", savedBooking.getId()));
    }
}
