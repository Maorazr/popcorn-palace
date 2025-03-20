package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeDto {

  private Long id;

  @NotNull(message = "Movie ID is mandatory")
  private Long movieId;

  @NotBlank(message = "Theater name is mandatory")
  private String theater;

  @Future(message = "Start time must be in the future")
  private LocalDateTime startTime;

  @Future(message = "End time must be in the future")
  private LocalDateTime endTime;

  @Positive(message = "Price must be positive")
  private double price;
}
