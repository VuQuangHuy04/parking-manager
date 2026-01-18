package org.example.DTO;

import java.time.LocalDateTime;

public class BookingDTO {
    private String userName;
    private String parkingName;
    private String slotCode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double moneyPaid;
    public BookingDTO(String userName, String parkingName, String slotCode, LocalDateTime startTime, LocalDateTime endTime, double moneyPaid) {
        this.userName = userName;
        this.parkingName = parkingName;
        this.slotCode = slotCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.moneyPaid = moneyPaid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getParkingName() {
        return parkingName;
    }

    public void setParkingName(String parkingName) {
        this.parkingName = parkingName;
    }

    public String getSlotCode() {
        return slotCode;
    }

    public void setSlotCode(String slotCode) {
        this.slotCode = slotCode;
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

    public double getMoneyPaid() {
        return moneyPaid;
    }

    public void setMoneyPaid(double moneyPaid) {
        this.moneyPaid = moneyPaid;
    }
}
