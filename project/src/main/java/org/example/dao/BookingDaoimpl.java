package org.example.dao;

import org.example.utils.DBConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingDaoimpl implements IBookingDao {
    // Định dạng chuẩn để SQLite so sánh chuỗi (YYYY-MM-DD HH:MM:SS)
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public boolean createBooking(int slotId, int userId, LocalDateTime endTime) {
        // Sử dụng ? cho cả start_time và end_time để ép kiểu String
        String sqlBooking = "INSERT INTO bookings (slot_id, user_id, start_time, end_time, status) VALUES (?, ?, ?, ?, 'ACTIVE')";
        String sqlSlot = "UPDATE parking_slots SET status = 'OCCUPIED' WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psB = conn.prepareStatement(sqlBooking);
                 PreparedStatement psS = conn.prepareStatement(sqlSlot)) {

                psB.setInt(1, slotId);
                psB.setInt(2, userId);
                // Ép thời gian về String để đồng nhất định dạng trong DB
                psB.setString(3, LocalDateTime.now().format(formatter));
                psB.setString(4, endTime.format(formatter));
                psB.executeUpdate();

                psS.setInt(1, slotId);
                psS.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public LocalDateTime getActiveBookingEndTime(int slotId) {
        String sql = "SELECT end_time FROM bookings WHERE slot_id = ? AND status = 'ACTIVE' ORDER BY id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Đọc String từ DB và chuyển ngược lại LocalDateTime cho UI
                return LocalDateTime.parse(rs.getString("end_time"), formatter);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void clearExpiredBookings() {
        String now = LocalDateTime.now().format(formatter);
        String sqlDeactivate = "UPDATE bookings SET status = 'DONE' WHERE end_time <= ? AND status = 'ACTIVE' AND end_time LIKE '202%'";
        String sqlReleaseSlot = "UPDATE parking_slots SET status = 'EMPTY' WHERE id IN " +
                "(SELECT slot_id FROM bookings WHERE end_time <= ? AND status = 'DONE' AND end_time LIKE '202%')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps1 = conn.prepareStatement(sqlDeactivate);
             PreparedStatement ps2 = conn.prepareStatement(sqlReleaseSlot)) {

            conn.setAutoCommit(false);
            ps1.setString(1, now);
            ps1.executeUpdate();

            ps2.setString(1, now);
            ps2.executeUpdate();

            conn.commit();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}