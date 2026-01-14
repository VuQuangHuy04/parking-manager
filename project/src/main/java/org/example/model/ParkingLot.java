package org.example.model;

public class ParkingLot {
    private int parkinglot_id;
    private String name;
    private String address;
    private int totalSlots;
    public ParkingLot(int parkinglot_id, String name, String address, int totalSlots) {
        this.parkinglot_id = parkinglot_id;
        this.name = name;
        this.address = address;
        this.totalSlots = totalSlots;
    }

    public int getParkinglot_id() {
        return parkinglot_id;
    }

    public void setParkinglot_id(int parkinglot_id) {
        this.parkinglot_id = parkinglot_id;
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
}
