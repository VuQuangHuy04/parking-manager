package org.example.DTO;

public class ParkingLotDTO {
    private int id; // Thêm ID để định danh bãi xe trong DB
    private String name;
    private String address;
    private double lat;
    private double lon;
    private double distance;

    public ParkingLotDTO(int id,String name, String address, double lat, double lon) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
    }

    // Getter cho ID
    public int getId() { return id; }
    public String getAddress() { return address; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }
    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    // Cần Setter để cập nhật tạm thời lat/lon sau khi gọi API
    public void setLat(double lat) { this.lat = lat; }
    public void setLon(double lon) { this.lon = lon; }
}
