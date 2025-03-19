package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;

import lombok.*;

@Data
public class MovieDto {

    @NotBlank
    private String title;

    @NotBlank
    private String genre;

    @Positive
    private int duration;

    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private float rating;

    @Min(1888) // first film year ever recorded :)
    private int releaseYear;
}
