package org.example.model;

import java.time.LocalDateTime;

public class Booking {
    private int booking_Id;
    private String userId;
    private int slotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double totalPrice;
    public Booking(String userId, int slotId, LocalDateTime startTime, LocalDateTime endTime) {
        this.userId = userId;
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getBooking_Id() {
        return booking_Id;
    }

    public void setBooking_Id(int booking_Id) {
        this.booking_Id = booking_Id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}
