package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto {

  private Long id;

  @NotBlank(message = "Movie title must not be empty")
  private String title;

  @NotBlank(message = "Genre is mandatory")
  private String genre;

  @Positive(message = "Duration must be positive")
  private int duration;

  @DecimalMin(value = "0.0", message = "Rating must be at least 0")
  @DecimalMax(value = "10.0", message = "Rating must be at most 10")
  private double rating;

  @Min(value = 1888, message = "Release year must be valid")
  // @Max(value = the current year?, message = "Release year must be valid")
  private int releaseYear;
}
