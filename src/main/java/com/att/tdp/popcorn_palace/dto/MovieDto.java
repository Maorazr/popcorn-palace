package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;

import lombok.*;

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

    @Positive
    private int duration;

    @DecimalMin(value = "0.0", message = "Rating must be 0 or higher")
    @DecimalMax(value = "10.0", message = "Rating must be 10 or lower")
    private double rating;

    @Min(value = 1888, message = "Release year must be realistic")
    private int releaseYear;
}
