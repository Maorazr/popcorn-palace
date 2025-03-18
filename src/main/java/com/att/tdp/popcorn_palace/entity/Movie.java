package com.att.tdp.popcorn_palace.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "movie")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private Double rating;

    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;
}


