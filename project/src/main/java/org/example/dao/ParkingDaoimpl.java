package org.example.dao;

import org.example.DTO.ParkingLotDTO;
import org.example.api.osmAPI;
import org.example.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class ParkingDaoimpl implements IParkingDao{
    @Override
    public List<String> getAddressList() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT address FROM parking_lots";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { // Thực thi truy vấn
            while (rs.next()) {
                String addr = rs.getString("address");
                list.add(addr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
     @Override
     public boolean UpdateLatandLon(String address) {
        Double[] coordinate = osmAPI.getCoordinateFromAddress(address);
        if (coordinate == null) {
            System.out.println("Không lấy được tọa độ");
            return false;
        }
        double lat = coordinate[0];
        double lon = coordinate[1];
        String sql = """
            INSERT INTO parking_lots( latitude, longitude) 
            VALUES (?, ?) where Address = ?
        """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, lat);
            ps.setDouble(2, lon);
            ps.setString(3, address);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public List<ParkingLotDTO> GetParkingDTO(){
        List<ParkingLotDTO> list = new ArrayList<>();
        String sql = "SELECT id, address, latitude, longitude FROM parking_lots";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String address = rs.getString("address");
                double lat = rs.getDouble("latitude");
                double lon = rs.getDouble("longitude");
                ParkingLotDTO dto = new ParkingLotDTO(id, address, lat, lon);
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    }
