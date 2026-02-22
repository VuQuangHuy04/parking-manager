CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL CHECK(role IN ('ADMIN','USER')),
    email TEXT,
    latitude REAL,
    longitude REAL
);
-- 2. Bảng Parking Lots
CREATE TABLE parking_lots (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    address TEXT NOT NULL,
    total_slots INTEGER NOT NULL CHECK(total_slots > 0),
    active INTEGER DEFAULT 1,
    latitude REAL,
    longitude REAL
);

-- 3. Bảng Parking Slots
CREATE TABLE parking_slots (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lot_id INTEGER,
    slot_code TEXT NOT NULL,
    x_pos REAL DEFAULT 0,
    y_pos REAL DEFAULT 0,
    status TEXT DEFAULT 'EMPTY' CHECK(status IN ('EMPTY', 'OCCUPIED', 'MAINTENANCE')),
    user_id INTEGER NULL,
    FOREIGN KEY (lot_id) REFERENCES parking_lots(id) ON DELETE CASCADE
);

-- 4. Bảng Bookings (Sửa tham chiếu khóa ngoại)
CREATE TABLE bookings (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    slot_id INTEGER NOT NULL,
    start_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    end_time DATETIME,
    total_price REAL DEFAULT 0,
    status TEXT DEFAULT 'ACTIVE' CHECK(status IN ('ACTIVE','DONE','PENDING','CANCELLED')),
     mail_sent INTEGER DEFAULT 0,
    payment_method TEXT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (slot_id) REFERENCES parking_slots(id)
);
INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'ADMIN'),
('user', '123', 'USER');
INSERT INTO parking_lots (name, address, total_slots) VALUES
('Bãi xe Lê Lợi', '123 Lê Lợi, Hà Nội', 10),
('Bãi xe Giải Phóng', '55 Giải Phóng, Hà Nội', 10),
('Bãi xe Nguyễn Trãi', '123 Nguyễn Trãi, Hà Nội', 10);
INSERT INTO parking_slots (lot_id, slot_code, x_pos, y_pos, status) VALUES
(1, 'A1', 50, 50, 'EMPTY'),
(1, 'A2', 150, 50, 'OCCUPIED'),
(1, 'A3', 250, 50, 'EMPTY'),
(1, 'A4', 350, 50, 'EMPTY'),
(1, 'A5', 450, 50, 'MAINTENANCE'),
(1, 'B1', 50, 150, 'EMPTY'),
(1, 'B2', 150, 150, 'EMPTY'),
(1, 'B3', 250, 150, 'OCCUPIED'),
(1, 'B4', 350, 150, 'EMPTY'),
(1, 'B5', 450, 150, 'EMPTY');
INSERT INTO parking_slots (lot_id, slot_code, x_pos, y_pos, status) VALUES
(2, 'G1', 100, 100, 'EMPTY'),
(2, 'G2', 200, 100, 'EMPTY'),
(2, 'G3', 300, 100, 'EMPTY'),
(2, 'G4', 400, 100, 'OCCUPIED'),
(2, 'G5', 500, 100, 'EMPTY');
INSERT INTO parking_slots (lot_id, slot_code, x_pos, y_pos, status) VALUES
(3, 'N1', 50, 50, 'EMPTY'),
(3, 'N2', 150, 50, 'EMPTY'),
(3, 'N3', 250, 50, 'OCCUPIED'),
(3, 'N4', 50, 150, 'EMPTY'),
(3, 'N5', 150, 150, 'EMPTY'),
(3, 'N6', 250, 150, 'EMPTY'),
(3, 'N7', 350, 50, 'MAINTENANCE'),
(3, 'N8', 350, 150, 'EMPTY'),
(3, 'N9', 450, 50, 'EMPTY'),
(3, 'N10', 450, 150, 'EMPTY');