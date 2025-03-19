package com.att.tdp.popcorn_palace.controller;


import com.att.tdp.popcorn_palace.dto.ShowtimeDto;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/showtimes")
@RequiredArgsConstructor
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    // GET /showtimes/{showtimeId}
    @GetMapping("/{showtimeId}")
    public ResponseEntity<ShowtimeDto> getShowtimeById(@PathVariable Long showtimeId) {
        ShowtimeDto showtime = showtimeService.getShowtimeById(showtimeId);
        return ResponseEntity.ok(showtime);
    }

    // POST /showtimes
    @PostMapping
    public ResponseEntity<ShowtimeDto> addShowtime(@Valid @RequestBody ShowtimeDto showtimeDto) {
        ShowtimeDto savedShowtime = showtimeService.createShowtime(showtimeDto);
        return ResponseEntity.ok(savedShowtime);
    }

    // POST /showtimes/update/{showtimeId}
    @PostMapping("/update/{showtimeId}")
    public ResponseEntity<Void> updateShowtime(@PathVariable Long showtimeId, @Valid @RequestBody ShowtimeDto updatedShowtimeDto) {
        showtimeService.updateShowtime(showtimeId, updatedShowtimeDto);
        return ResponseEntity.ok().build();
    }

    // DELETE /showtimes/{showtimeId}
    @DeleteMapping("/{showtimeId}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long showtimeId) {
        showtimeService.deleteShowtime(showtimeId);
        return ResponseEntity.ok().build();
    }


    private ShowtimeDto convertToDto(Showtime showtime) {
        return ShowtimeDto.builder()
                .id(showtime.getId())
                .movieId(showtime.getMovie().getId())
                .theater(showtime.getTheater())
                .startTime(showtime.getStartTime())
                .endTime(showtime.getEndTime())
                .price(showtime.getPrice())
                .build();
    }
}
