-- V1__Initial_Schema.sql
-- Users table
CREATE TABLE IF NOT EXISTS users (
                                     id BIGSERIAL PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    enabled BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Genres table
CREATE TABLE IF NOT EXISTS genres (
                                      id BIGSERIAL PRIMARY KEY,
                                      name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
    );

-- Movies table
CREATE TABLE IF NOT EXISTS movies (
                                      id BIGSERIAL PRIMARY KEY,
                                      title VARCHAR(255) NOT NULL,
    description TEXT,
    poster_url VARCHAR(500),
    duration INTEGER,
    language VARCHAR(50),
    rating VARCHAR(10),
    release_date TIMESTAMP,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Movie-Genre junction table
CREATE TABLE IF NOT EXISTS movie_genres (
                                            movie_id BIGINT REFERENCES movies(id) ON DELETE CASCADE,
    genre_id BIGINT REFERENCES genres(id) ON DELETE CASCADE,
    PRIMARY KEY (movie_id, genre_id)
    );

-- Showtimes table
CREATE TABLE IF NOT EXISTS showtimes (
                                         id BIGSERIAL PRIMARY KEY,
                                         movie_id BIGINT NOT NULL REFERENCES movies(id),
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    total_seats INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    hall_number VARCHAR(50),
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_time_range CHECK (end_time > start_time)
    );

-- Seats table
CREATE TABLE IF NOT EXISTS seats (
                                     id BIGSERIAL PRIMARY KEY,
                                     showtime_id BIGINT NOT NULL REFERENCES showtimes(id) ON DELETE CASCADE,
    seat_number VARCHAR(10) NOT NULL,
    row_number VARCHAR(5),
    seat_column INTEGER,
    status VARCHAR(20) DEFAULT 'AVAILABLE',
    UNIQUE(showtime_id, seat_number)
    );

-- Reservations table
CREATE TABLE IF NOT EXISTS reservations (
                                            id BIGSERIAL PRIMARY KEY,
                                            user_id BIGINT NOT NULL REFERENCES users(id),
    showtime_id BIGINT NOT NULL REFERENCES showtimes(id),
    seat_id BIGINT NOT NULL UNIQUE REFERENCES seats(id),
    booking_reference VARCHAR(50) UNIQUE,
    total_price DECIMAL(10,2),
    status VARCHAR(50) DEFAULT 'CONFIRMED',
    reservation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiry_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT valid_status CHECK (status IN ('CONFIRMED', 'PENDING_PAYMENT', 'CANCELLED', 'COMPLETED', 'EXPIRED'))
    );

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
                                        id BIGSERIAL PRIMARY KEY,
                                        reservation_id BIGINT NOT NULL UNIQUE REFERENCES reservations(id),
    amount DECIMAL(10,2),
    payment_method VARCHAR(50),
    transaction_id VARCHAR(100),
    status VARCHAR(20) DEFAULT 'PENDING',
    payment_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Indexes for performance
CREATE INDEX idx_showtimes_start_time ON showtimes(start_time);
CREATE INDEX idx_showtimes_movie_id ON showtimes(movie_id);
CREATE INDEX idx_reservations_user_id ON reservations(user_id);
CREATE INDEX idx_reservations_showtime_id ON reservations(showtime_id);
CREATE INDEX idx_seats_showtime_id_status ON seats(showtime_id, status);