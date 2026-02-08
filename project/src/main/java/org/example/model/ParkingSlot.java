package org.example.model;

import org.example.constant.VehicleType;

public class ParkingSlot {
    private int id;
    private int lotId;
    private String slotCode;
    private double x;
    private double y;
    private String status;
    public ParkingSlot(int id, int lotId, String slotCode, double x, double y, String status) {
        this.id = id;
        this.lotId = lotId;
        this.slotCode = slotCode;
        this.x = x;
        this.y = y;
        this.status = status;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLotId() {
        return lotId;
    }

    public void setLotId(int lotId) {
        this.lotId = lotId;
    }

    public String getSlotCode() {
        return slotCode;
    }

    public void setSlotCode(String slotCode) {
        this.slotCode = slotCode;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
