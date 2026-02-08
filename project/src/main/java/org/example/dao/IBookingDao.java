package org.example.dao;

import org.example.DTO.BookingDTO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface IBookingDao {
    boolean createBooking(int slotId, int userId, LocalDateTime endTime);
    LocalDateTime getActiveBookingEndTime(int slotId);
    void clearExpiredBookings();
}
