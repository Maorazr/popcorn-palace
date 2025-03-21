DELETE
FROM booking;
DELETE
FROM showtime;
DELETE
FROM movie;

ALTER SEQUENCE movie_id_seq RESTART WITH 1;
ALTER SEQUENCE showtime_id_seq RESTART WITH 1;

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


INSERT INTO showtime (movie_id, theater, start_time, end_time, price)
VALUES (1, 'Cinema 1', '2025-03-21 18:00:00', '2025-03-21 20:28:00', 15.00),
       (2, 'Cinema 1', '2025-03-21 21:00:00', '2025-03-21 23:32:00', 14.50),
       (3, 'Cinema 2', '2025-03-21 17:30:00', '2025-03-21 20:19:00', 16.00),
       (4, 'Cinema 3', '2025-03-21 19:00:00', '2025-03-21 21:34:00', 13.00),
       (5, 'Cinema 2', '2025-03-22 18:00:00', '2025-03-22 20:55:00', 18.00),
       (6, 'Cinema 1', '2025-03-22 20:30:00', '2025-03-22 22:49:00', 12.00),
       (7, 'Cinema 4', '2025-03-22 21:00:00', '2025-03-22 23:16:00', 14.00),
       (8, 'Cinema 3', '2025-03-23 17:00:00', '2025-03-23 19:22:00', 13.50),
       (9, 'Cinema 5', '2025-03-23 20:00:00', '2025-03-23 22:35:00', 15.00),
       (10, 'Cinema 1', '2025-03-24 19:00:00', '2025-03-24 22:15:00', 17.00);
