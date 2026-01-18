package org.example.model;

import org.example.constant.BookingStatus;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private int userId;
    private int lotId;
    private int slotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalPrice;
    private BookingStatus bookingStatus;

    public Booking(int id, int userId, int lotId, int slotId, LocalDateTime startTime, LocalDateTime endTime, double totalPrice, BookingStatus bookingStatus) {
        this.id = id;
        this.userId = userId;
        this.lotId = lotId;
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalPrice = totalPrice;
        this.bookingStatus = bookingStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLotId() {
        return lotId;
    }

    public void setLotId(int lotId) {
        this.lotId = lotId;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(BookingStatus bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
