package org.example.dao;

import org.example.model.ParkingSlot;
import org.example.utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SlotDaoimpl implements ISlotDao{
    @Override
    public List<ParkingSlot> getAllSlots(){
        List<ParkingSlot> list = new ArrayList<>();
        String sql = "SELECT * FROM parking_slots";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new ParkingSlot(
                        rs.getInt("id"),
                        rs.getInt("lot_id"),
                        rs.getString("slot_code"),
                        rs.getDouble("x_pos"),
                        rs.getDouble("y_pos"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public List<ParkingSlot> getSlotsByLotId(int lotId) {
        List<ParkingSlot> list = new ArrayList<>();
        String sql = "SELECT * FROM parking_slots WHERE lot_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lotId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ParkingSlot(
                        rs.getInt("id"),
                        rs.getInt("lot_id"),
                        rs.getString("slot_code"),
                        rs.getDouble("x_pos"),
                        rs.getDouble("y_pos"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override
    public boolean insertSlot(ParkingSlot slot) {
        String sql = "INSERT INTO parking_slots (lot_id, slot_code, x_pos, y_pos, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slot.getLotId());
            ps.setString(2, slot.getSlotCode());
            ps.setDouble(3, slot.getX());
            ps.setDouble(4, slot.getY());
            ps.setString(5, slot.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public void updateSlotPosition(int lotId, String code, double x, double y) {
        String sql = "UPDATE parking_slots SET x_pos = ?, y_pos = ? WHERE lot_id = ? AND slot_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, x);
            ps.setDouble(2, y);
            ps.setInt(3, lotId);
            ps.setString(4, code);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public boolean deleteSlot(int slotId) {
        String sql = "DELETE FROM parking_slots WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    @Override
    public boolean updateStatus(int slotId, String status, int userId) {
        return false;
    }
    public void setOccupied(int slotId, int userId) throws SQLException {
        String sql = "UPDATE parking_slots SET status='OCCUPIED', user_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, slotId);
            ps.executeUpdate();
        }
    }
    public void setEmpty(int slotId) throws SQLException {
        String sql = "UPDATE parking_slots SET status='EMPTY', user_id=NULL WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            ps.executeUpdate();
        }
    }
}