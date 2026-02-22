package org.example.dao;

import org.example.DTO.BookingDTO;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface IBookingDao {
    public boolean createBooking(int slotId, int userId, int hours, String paymentMethod);
    public LocalDateTime getActiveBookingEndTime(int slotId);
    public void clearExpiredBookings(LocalDateTime now);
}
