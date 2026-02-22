package org.example.DTO;

import java.time.LocalDateTime;

public class BookingDTO {
    private int id;
    private String userName;
    private String parkingName;
    private String slotCode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double moneyPaid;
    private String email;
    private int slotId;

    public BookingDTO(int id, String userName, String parkingName, String slotCode, LocalDateTime startTime, LocalDateTime endTime, double moneyPaid) {
        this.id = id;
        this.userName = userName;
        this.parkingName = parkingName;
        this.slotCode = slotCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.moneyPaid = moneyPaid;
    }
    public BookingDTO() {
    }
    public int getUserId() {
        return this.id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public int getSlotId() {
        return slotId;
    }
    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }
}
