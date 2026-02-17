package org.example.dao;

import org.example.DTO.BookingDTO;
import org.example.utils.DBConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BookingDaoimpl implements IBookingDao {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public boolean createBooking(int slotId, int userId, LocalDateTime endTime) {
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
    public List<BookingDTO> userHistory(int userId) {
        List<BookingDTO> historyList = new ArrayList<>();
        String sql = """
    SELECT u.username, 
           p.name AS parking_name, 
           s.slot_code, 
           b.start_time, 
           b.end_time, 
           b.total_price
    FROM bookings b
    JOIN users u ON b.user_id = u.id
    JOIN parking_slots s ON b.slot_id = s.id
    JOIN parking_lots p ON s.lot_id = p.id
    WHERE b.user_id = ?
    ORDER BY b.id DESC
""";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String userName = rs.getString("username");
                String parkingName = rs.getString("parking_name");
                String slotCode = rs.getString("slot_code");
                LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
                LocalDateTime endTime = rs.getTimestamp("end_time") != null
                        ? rs.getTimestamp("end_time").toLocalDateTime()
                        : null;
                double moneyPaid = rs.getDouble("total_price");
                historyList.add(new BookingDTO(
                        userName,
                        parkingName,
                        slotCode,
                        startTime,
                        endTime,
                        moneyPaid
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return historyList;
    }
    // Thống kê doanh thu theo từng ngày trong một tháng cụ thể
    public Map<String, Double> getRevenueByDay(String yearMonth) { // format "yyyy-MM"
        Map<String, Double> map = new LinkedHashMap<>();
        String sql = """
        SELECT strftime('%d/%m', end_time) as day, SUM(total_price) as revenue
        FROM bookings
        WHERE status = 'DONE' AND end_time LIKE ?
        GROUP BY day ORDER BY day ASC
    """;
        return executeRevenueQuery(sql, yearMonth + "%");
    }
    // Thống kê doanh thu theo từng tháng trong một năm
    public Map<String, Double> getRevenueByMonth(String year) { // format "yyyy"
        Map<String, Double> map = new LinkedHashMap<>();
        String sql = """
        SELECT strftime('%m/%Y', end_time) as month, SUM(total_price) as revenue
        FROM bookings
        WHERE status = 'DONE' AND end_time LIKE ?
        GROUP BY month ORDER BY month ASC
    """;
        return executeRevenueQuery(sql, year + "%");
    }
    // Hàm phụ để chạy query và trả về Map
    private Map<String, Double> executeRevenueQuery(String sql, String param) {
        Map<String, Double> map = new LinkedHashMap<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, param);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString(1), rs.getDouble(2));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }
}