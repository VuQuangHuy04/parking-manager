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
    public List<ParkingLotDTO> getParkingWithoutCoordinate() {
        List<ParkingLotDTO> list = new ArrayList<>();
        String sql = """
        SELECT id, name, address 
        FROM parking_lots
        WHERE latitude IS NULL OR longitude IS NULL
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new ParkingLotDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        0, 0
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
     @Override
     public void updateLatLonById(int id, double lat, double lon) {
         String sql = "UPDATE parking_lots SET latitude=?, longitude=? WHERE id=?";

         try (Connection conn = DBConnection.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)) {

             ps.setDouble(1, lat);
             ps.setDouble(2, lon);
             ps.setInt(3, id);
             ps.executeUpdate();

         } catch (Exception e) {
             e.printStackTrace();
         }
     }


    @Override
    public List<ParkingLotDTO> getAllParkingDTO() {
        List<ParkingLotDTO> list = new ArrayList<>();
        String sql = "SELECT id, name, address, latitude, longitude FROM parking_lots";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new ParkingLotDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    }
