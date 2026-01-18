package org.example.model;

import org.example.constant.VehicleType;

public class ParkingSlot {
    private int id;
    private int parkinglot_id;
    private String slotCode;
    private boolean available;

    public ParkingSlot(int id, int parkinglot_id, String slotCode, boolean available) {
        this.id = id;
        this.parkinglot_id = parkinglot_id;
        this.slotCode = slotCode;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParkinglot_id() {
        return parkinglot_id;
    }

    public void setParkinglot_id(int parkinglot_id) {
        this.parkinglot_id = parkinglot_id;
    }

    public String getSlotCode() {
        return slotCode;
    }

    public void setSlotCode(String slotCode) {
        this.slotCode = slotCode;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
