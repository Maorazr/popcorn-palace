package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.ShowtimeDto;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
import com.att.tdp.popcorn_palace.exception.OverlappingShowtimeException;
import com.att.tdp.popcorn_palace.exception.ResourceNotFoundException;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShowtimeService {

  private final ShowtimeRepository showtimeRepository;
  private final MovieRepository movieRepository;

  public ShowtimeDto getShowtimeById(Long showtimeId) {
    return entityToDto(findShowtimeById(showtimeId));
  }

  public ShowtimeDto createShowtime(ShowtimeDto showtimeDto) {
    Movie movie = findMovieById(showtimeDto.getMovieId());

    boolean hasConflict =
        showtimeRepository.existsOverlappingShowtime(
            -1L, showtimeDto.getTheater(), showtimeDto.getStartTime(), showtimeDto.getEndTime());

    if (hasConflict) {
      throw new OverlappingShowtimeException(
          "Cannot create showtime; it overlaps in theater " + showtimeDto.getTheater());
    }

    Showtime showtime = dtoToEntity(showtimeDto, movie);
    Showtime savedShowtime = showtimeRepository.save(showtime);

    return entityToDto(savedShowtime);
  }

  public void updateShowtime(Long showtimeId, ShowtimeDto showtimeDto) {
    Showtime existingShowtime = findShowtimeById(showtimeId);

    Movie movie = findMovieById(showtimeDto.getMovieId());

    existingShowtime.setMovie(movie);
    existingShowtime.setTheater(showtimeDto.getTheater());
    existingShowtime.setStartTime(showtimeDto.getStartTime());
    existingShowtime.setEndTime(showtimeDto.getEndTime());
    existingShowtime.setPrice(showtimeDto.getPrice());

    showtimeRepository.save(existingShowtime);
  }

  public void deleteShowtime(Long showtimeId) {
    showtimeRepository.delete(findShowtimeById(showtimeId));
  }

  private Showtime findShowtimeById(Long showtimeId) {
    return showtimeRepository
        .findById(showtimeId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    "Showtime not found\n" + "showtimeId: " + showtimeId));
  }

  private Movie findMovieById(Long movieId) {
    return movieRepository
        .findById(movieId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Movie not found\n" + "movieId: " + movieId));
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
