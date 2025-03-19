package com.att.tdp.popcorn_palace.dto;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class ShowtimeDto {

    @NotNull
    private Long movieId;

    @NotBlank
    private String theater;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    @Positive
    private float price;
}
