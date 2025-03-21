package com.att.tdp.popcorn_palace.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import com.att.tdp.popcorn_palace.util.ResponseUtils;
import com.att.tdp.popcorn_palace.dto.ShowtimeDto;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/showtimes")
@RequiredArgsConstructor
public class ShowtimeController {

  private final ShowtimeService showtimeService;

  @GetMapping("/{showtimeId}")
  public ResponseEntity<ShowtimeDto> getShowtimeById(@PathVariable Long showtimeId) {
    ShowtimeDto showtime = showtimeService.getShowtimeById(showtimeId);
    return ResponseEntity.ok(showtime);
  }

  @PostMapping
  public ResponseEntity<ShowtimeDto> addShowtime(@Valid @RequestBody ShowtimeDto showtimeDto) {
    ShowtimeDto savedShowtime = showtimeService.addShowtime(showtimeDto);
    return ResponseEntity.ok(savedShowtime);
  }

  @PostMapping("/update/{showtimeId}")
  public ResponseEntity<Void> updateShowtime(
      @PathVariable Long showtimeId, @Valid @RequestBody ShowtimeDto updatedShowtimeDto) {
    showtimeService.updateShowtime(showtimeId, updatedShowtimeDto);
    return ResponseUtils.ok();
  }

  @DeleteMapping("/{showtimeId}")
  public ResponseEntity<Void> deleteShowtime(@PathVariable Long showtimeId) {
    showtimeService.deleteShowtime(showtimeId);
    return ResponseUtils.ok();
  }
}
