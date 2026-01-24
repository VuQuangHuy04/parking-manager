-- Tạo bảng Users
CREATE TABLE users (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       username TEXT UNIQUE,
                       password TEXT,
                       role TEXT,
                       latitude DOUBLE,
                       longitude DOUBLE
);
-- Tạo bảng Parking Lots
CREATE TABLE parking_lots (
                              id INTEGER PRIMARY KEY AUTOINCREMENT,
                              name TEXT,
                              address TEXT,
                              total_slots INTEGER,
                              active BOOLEAN,
                              latitude DOUBLE,
                              longitude DOUBLE

);

-- Tạo bảng Parking Slots
CREATE TABLE parking_slots (
                               slot_id TEXT PRIMARY KEY,
                               lot_id INTEGER,
                               status TEXT DEFAULT 'EMPTY', -- EMPTY, BOOKED
                               vehicle_type TEXT,
                               FOREIGN KEY (lot_id) REFERENCES parking_lots(id)
);

-- Tạo bảng Bookings
CREATE TABLE bookings (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          username TEXT,
                          slot_id TEXT,
                          start_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                          total_price REAL,
                          FOREIGN KEY (slot_id) REFERENCES parking_slots(slot_id)
);

-- Chèn dữ liệu mẫu
INSERT INTO users (username, password, role) VALUES
                                                 ('admin', 'admin123', 'ADMIN'),
                                                 ('user', '123', 'USER');

INSERT INTO parking_lots (name, address, total_slots) VALUES
    ('Bãi xe Lê Lợi', '123 Lê Lợi, hà nội', 10);
INSERT INTO parking_lots (name, address, total_slots) VALUES
    ('Bãi xe Giải Phóng', '55 Giải Phóng, hà nội', 10);
INSERT INTO parking_lots (name, address, total_slots) VALUES
    ('Bãi xe Nguyễn Trãi', '123 Nguyễn Trãi, hà nội', 10);