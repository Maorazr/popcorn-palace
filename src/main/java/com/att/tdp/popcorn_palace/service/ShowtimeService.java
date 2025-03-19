package com.att.tdp.popcorn_palace.service;


import com.att.tdp.popcorn_palace.dto.ShowtimeDto;
import com.att.tdp.popcorn_palace.entity.Movie;
import com.att.tdp.popcorn_palace.entity.Showtime;
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
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        return entityToDto(showtime);
    }

    public ShowtimeDto createShowtime(ShowtimeDto showtimeDto) {
        Movie movie = movieRepository.findById(showtimeDto.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Showtime showtime = dtoToEntity(showtimeDto, movie);
        Showtime savedShowtime = showtimeRepository.save(showtime);

        return entityToDto(savedShowtime);
    }

    public void updateShowtime(Long showtimeId, ShowtimeDto showtimeDto) {
        Showtime existingShowtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        Movie movie = movieRepository.findById(showtimeDto.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        existingShowtime.setMovie(movie);
        existingShowtime.setTheater(showtimeDto.getTheater());
        existingShowtime.setStartTime(showtimeDto.getStartTime());
        existingShowtime.setEndTime(showtimeDto.getEndTime());
        existingShowtime.setPrice(showtimeDto.getPrice());

        showtimeRepository.save(existingShowtime);
    }

    public void deleteShowtime(Long showtimeId) {
        if (!showtimeRepository.existsById(showtimeId)) {
            throw new RuntimeException("Showtime not found");
        }
        showtimeRepository.deleteById(showtimeId);
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
