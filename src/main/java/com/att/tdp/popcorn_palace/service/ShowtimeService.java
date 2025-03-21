package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.exception.MovieNotFoundException;
import com.att.tdp.popcorn_palace.exception.OverlappingShowtimeException;
import com.att.tdp.popcorn_palace.exception.ShowtimeNotFoundException;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.dto.ShowtimeDto;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.entity.Movie;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ShowtimeService {

  private final ShowtimeRepository showtimeRepository;
  private final MovieRepository movieRepository;

  public ShowtimeDto getShowtimeById(Long id) {
    return entityToDto(findShowtimeById(id));
  }

  public ShowtimeDto addShowtime(ShowtimeDto showtimeDto) {
    Movie movie = findMovieById(showtimeDto.getMovieId());

    boolean hasConflict =
        showtimeRepository.existsOverlappingShowtime(
            -1L, showtimeDto.getTheater(), showtimeDto.getStartTime(), showtimeDto.getEndTime());

    if (hasConflict) {
      throw new OverlappingShowtimeException(showtimeDto.getTheater());
    }

    Showtime showtime = dtoToEntity(showtimeDto, movie);
    Showtime savedShowtime = showtimeRepository.save(showtime);

    return entityToDto(savedShowtime);
  }

  public void updateShowtime(Long id, ShowtimeDto showtimeDto) {
    Showtime existingShowtime = findShowtimeById(id);

    Movie movie = findMovieById(showtimeDto.getMovieId());

    existingShowtime.setMovie(movie);
    existingShowtime.setTheater(showtimeDto.getTheater());
    existingShowtime.setStartTime(showtimeDto.getStartTime());
    existingShowtime.setEndTime(showtimeDto.getEndTime());
    existingShowtime.setPrice(showtimeDto.getPrice());

    showtimeRepository.save(existingShowtime);
  }

  public void deleteShowtime(Long id) {
    showtimeRepository.delete(findShowtimeById(id));
  }

  private Showtime findShowtimeById(Long id) {
    return showtimeRepository.findById(id).orElseThrow(() -> new ShowtimeNotFoundException(id));
  }

  private Movie findMovieById(Long id) {
    return movieRepository.findById(id).orElseThrow(() -> new MovieNotFoundException(id));
  }

  private Showtime dtoToEntity(ShowtimeDto dto, Movie movie) {
    return Showtime.builder()
        .movie(movie)
        .theater(dto.getTheater())
        .startTime(dto.getStartTime())
        .endTime(dto.getEndTime())
        .price(dto.getPrice())
        .build();
  }

  private ShowtimeDto entityToDto(Showtime showtime) {
    return ShowtimeDto.builder()
        .id(showtime.getId())
        .movieId(showtime.getMovie().getId())
        .theater(showtime.getTheater())
        .startTime(showtime.getStartTime())
        .endTime(showtime.getEndTime())
        .price(showtime.getPrice())
        .build();
  }
}
