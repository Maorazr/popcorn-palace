package com.att.tdp.popcorn_palace.controller;


import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    private final ShowtimeRepository showtimeRepository;

    public ShowtimeController(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    // GET /showtimes/{showtimeId}
    @GetMapping("/{showtimeId}")
    public ResponseEntity<Showtime> getShowtimeById(@PathVariable Long showtimeId) {
        return showtimeRepository.findById(showtimeId)
                .map(ResponseEntity::ok)
                .orElseGet(() ->  ResponseEntity.notFound().build());
    }

    // POST /showtimes
    @PostMapping
    public ResponseEntity<Showtime> addShowtime(@RequestBody Showtime showtime) {
        Showtime savedShowtime = showtimeRepository.save(showtime);
        return ResponseEntity.ok(savedShowtime);
    }

    // POST /showtimes/update/{showtimeId}
    @PostMapping("/update/{showtimeId}")
    public ResponseEntity<Void> updateShowtime(@PathVariable Long showtimeId, @RequestBody Showtime updatedShowtime) {
        Optional<Showtime> existing = showtimeRepository.findById(showtimeId);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Showtime existingShowtime = existing.get();
        existingShowtime.setMovie(updatedShowtime.getMovie());
        existingShowtime.setTheater(updatedShowtime.getTheater());
        existingShowtime.setStartTime(updatedShowtime.getStartTime());
        existingShowtime.setEndTime(updatedShowtime.getEndTime());
        existingShowtime.setPrice(updatedShowtime.getPrice());

        return ResponseEntity.ok().build();
    }

    // DELETE /showtimes/{showtimeId}
    @DeleteMapping("/{showtimeId}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long showtimeId) {
        if (!showtimeRepository.existsById(showtimeId)) {
            return ResponseEntity.notFound().build();
        }
        showtimeRepository.deleteById(showtimeId);
        return ResponseEntity.ok().build();
    }
}
