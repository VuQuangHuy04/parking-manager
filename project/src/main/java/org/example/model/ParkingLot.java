package org.example.model;

public class ParkingLot {
    private int id;
    private String name;
    private String address;
    private int totalSlots;
    private boolean active;
    public ParkingLot(int id, String name, String address, int totalSlots, boolean active) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.totalSlots = totalSlots;
        this.active = active;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}