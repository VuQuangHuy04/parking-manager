package org.example.model;

import org.example.constant.VehicleType;

public class ParkingSlot {
    private String Slotid;
    private VehicleType type;
    private boolean available;
    public ParkingSlot(String slotid, VehicleType type, boolean available) {
        this.Slotid = slotid;
        this.type = type;
        this.available = available;
    }

    public String getSlotid() {
        return Slotid;
    }

    public void setSlotid(String slotid) {
        Slotid = slotid;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
