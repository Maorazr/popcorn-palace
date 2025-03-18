package com.att.tdp.popcorn_palace.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "booking", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"showtime_id", "seat_number"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(name = "user_id", nullable = false)
    private UUID userId;
}

