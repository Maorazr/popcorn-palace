package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {

  private UUID id; // for response only

  @NotNull(message = "Showtime ID is required")
  private Long showtimeId;

  @NotNull(message = "Seat number is required")
  @Positive(message = "Seat number must be positive")
  private int seatNumber;

  @NotNull(message = "User ID is required")
  private UUID userId;
}
