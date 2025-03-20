CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS movie
(
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(255) UNIQUE NOT NULL,
    genre        VARCHAR(100)        NOT NULL,
    duration     INTEGER             NOT NULL,
    rating       DECIMAL(3, 1)       NOT NULL,
    release_year INTEGER             NOT NULL
);


CREATE TABLE IF NOT EXISTS showtime
(
    id         SERIAL PRIMARY KEY,
    movie_id   INTEGER       NOT NULL REFERENCES movie (id) ON DELETE CASCADE,
    theater    VARCHAR(100)  NOT NULL,
    start_time TIMESTAMP     NOT NULL,
    end_time   TIMESTAMP     NOT NULL,
    price      DECIMAL(5, 2) NOT NULL,
    UNIQUE (theater, start_time, end_time)
);

CREATE TABLE IF NOT EXISTS booking
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    showtime_id INTEGER NOT NULL REFERENCES showtime (id) ON DELETE CASCADE,
    seat_number INTEGER NOT NULL,
    user_id     UUID    NOT NULL,
    UNIQUE (showtime_id, seat_number)
);
