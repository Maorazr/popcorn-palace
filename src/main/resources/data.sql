DELETE
FROM booking;
DELETE
FROM showtime;
DELETE
FROM movie;

ALTER SEQUENCE movie_id_seq RESTART WITH 1;

INSERT INTO movie (title, genre, duration, rating, release_year)
VALUES ('Inception', 'Sci-Fi', 148, 8.8, 2010),
       ('The Dark Knight', 'Action', 152, 9.0, 2008),
       ('Interstellar', 'Sci-Fi', 169, 8.7, 2014),
       ('Pulp Fiction', 'Crime', 154, 8.9, 1994),
       ('The Godfather', 'Crime', 175, 9.2, 1972),
       ('Fight Club', 'Drama', 139, 8.8, 1999),
       ('The Matrix', 'Sci-Fi', 136, 8.7, 1999),
       ('Forrest Gump', 'Drama', 142, 8.8, 1994),
       ('Gladiator', 'Action', 155, 8.5, 2000),
       ('Titanic', 'Romance', 195, 7.9, 1997);


-- assuming first autogenerated id will be 1
INSERT INTO showtime (movie_id, theater, start_time, end_time, price)
VALUES (1, 'Cinema 1', '2025-03-20 20:00:00', '2025-03-20 22:16:00', 12.50);
