package org.example.service;

import org.example.DTO.BookingDTO;
import org.example.dao.BookingDaoimpl;
import org.example.dao.SlotDaoimpl;
import org.example.model.ParkingSlot;

import java.time.LocalDateTime;
import java.util.List;

public class BookingService {
    BookingDaoimpl bookingDaoimpl = new BookingDaoimpl();
    public List<BookingDTO> OutHistory(int userid){
        return bookingDaoimpl.userHistory(userid);
    }
}
