package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {

  private UUID id;

  @NotNull(message = "Showtime ID is required")
  private Long showtimeId;

  @NotNull(message = "Seat number is required")
  @Positive(message = "Seat number must be positive")
  private int seatNumber;

  @NotNull(message = "User ID is required")
  private UUID userId;
}
