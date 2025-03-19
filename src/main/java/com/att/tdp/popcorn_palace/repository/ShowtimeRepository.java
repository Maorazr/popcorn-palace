package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.entity.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {
    @Query("""
      SELECT COUNT(s) > 0
      FROM Showtime s
      WHERE s.theater = :theater
        AND s.id <> :showtimeId
        AND (s.startTime < :endTime AND s.endTime > :startTime)
    """)
    boolean existsOverlappingShowtime(
            Long showtimeId,
            String theater,
            LocalDateTime startTime,
            LocalDateTime endTime
    );
}
