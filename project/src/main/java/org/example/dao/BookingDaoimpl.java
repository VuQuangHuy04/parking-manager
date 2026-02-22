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
    public boolean createBooking(int slotId, int userId, int totalMinutes, String paymentMethod) {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(totalMinutes);
        // Giá theo phút
        double pricePerMinute = 5000.0 / 60.0;
        double totalPrice = Math.ceil(totalMinutes * pricePerMinute);
        String sql = """
        INSERT INTO bookings 
        (slot_id, user_id, start_time, end_time, status, total_price, payment_method) 
        VALUES (?, ?, ?, ?, 'PENDING', ?, ?)
    """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            ps.setInt(2, userId);
            ps.setString(3, startTime.format(formatter));
            ps.setString(4, endTime.format(formatter));
            ps.setDouble(5, totalPrice);
            ps.setString(6, paymentMethod);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean confirmPayment(int slotId) {

        String sqlBooking = """
        UPDATE bookings
        SET status = 'ACTIVE'
        WHERE id = (
        SELECT id FROM bookings
        WHERE slot_id = ? AND status = 'PENDING'
        ORDER BY id DESC LIMIT 1 )
    """;
        String sqlSlot = """
        UPDATE parking_slots 
        SET status = 'OCCUPIED' 
        WHERE id = ?
    """;
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psB = conn.prepareStatement(sqlBooking);
                 PreparedStatement psS = conn.prepareStatement(sqlSlot)) {
                psB.setInt(1, slotId);
                psB.executeUpdate();
                psS.setInt(1, slotId);
                psS.executeUpdate();
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public void clearExpiredBookings(LocalDateTime now) {

        String sqlGetExpiredSlots = """
        SELECT DISTINCT slot_id
        FROM bookings
        WHERE end_time <= ?
        AND status = 'ACTIVE'
    """;

        String sqlDeactivateBooking = """
        UPDATE bookings
        SET status = 'DONE'
        WHERE end_time <= ?
        AND status = 'ACTIVE'
    """;

        // Chỉ release slot nếu KHÔNG còn booking ACTIVE
        String sqlReleaseSlot = """
        UPDATE parking_slots
        SET status = 'EMPTY'
        WHERE id = ?
        AND NOT EXISTS (
            SELECT 1 FROM bookings
            WHERE slot_id = ?
            AND status = 'ACTIVE'
        )
    """;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            List<Integer> slotIds = new ArrayList<>();
            // 1. Lấy danh sách slot có booking hết hạn
            try (PreparedStatement ps = conn.prepareStatement(sqlGetExpiredSlots)) {
                ps.setString(1, now.format(formatter));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    slotIds.add(rs.getInt("slot_id"));
                }
            }
            // 2. Chuyển booking hết hạn -> DONE
            try (PreparedStatement ps = conn.prepareStatement(sqlDeactivateBooking)) {
                ps.setString(1, now.format(formatter));
                ps.executeUpdate();
            }
            // 3. Release slot nhưng chỉ khi không còn ACTIVE
            try (PreparedStatement ps = conn.prepareStatement(sqlReleaseSlot)) {
                for (Integer slotId : slotIds) {
                    ps.setInt(1, slotId);
                    ps.setInt(2, slotId);
                    ps.executeUpdate();
                }
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BookingDTO> userHistory(int userId) {
        List<BookingDTO> historyList = new ArrayList<>();
        String sql = """
     SELECT u.id AS user_id,
               u.username,
               p.name AS parking_name,
               s.slot_code,
               b.start_time,
               b.end_time,
               b.total_price,
               b.status
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

                historyList.add(new BookingDTO(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("parking_name"),
                        rs.getString("slot_code"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time") != null
                                ? rs.getTimestamp("end_time").toLocalDateTime()
                                : null,
                        rs.getDouble("total_price")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return historyList;
    }
    public List<BookingDTO> getAllBookings() {
        List<BookingDTO> historyList = new ArrayList<>();
        String sql = """
        SELECT u.id AS user_id,
               u.username,
               p.name AS parking_name,
               s.slot_code,
               b.start_time,
               b.end_time,
               b.total_price,
               b.status
        FROM bookings b
        JOIN users u ON b.user_id = u.id
        JOIN parking_slots s ON b.slot_id = s.id
        JOIN parking_lots p ON s.lot_id = p.id
        ORDER BY b.id DESC
    """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                historyList.add(new BookingDTO(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("parking_name"),
                        rs.getString("slot_code"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getTimestamp("end_time") != null
                                ? rs.getTimestamp("end_time").toLocalDateTime()
                                : null,
                        rs.getDouble("total_price")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return historyList;
    }
    // Thống kê doanh thu theo từng ngày trong một tháng cụ thể
    public Map<String, Double> getRevenueByDay(String yearMonth) {

        String sql = """
        SELECT strftime('%d/%m', start_time) as day,
               IFNULL(SUM(total_price),0) as revenue
        FROM bookings
        WHERE status IN ('ACTIVE','DONE')
        AND strftime('%Y-%m', start_time) = ?
        GROUP BY day
        ORDER BY day ASC
    """;

        return executeRevenueQuery(sql, yearMonth);
    }

    public Map<String, Double> getRevenueByMonth(String year) {
        String sql = """
        SELECT strftime('%m/%Y', start_time) as month,
               IFNULL(SUM(total_price),0) as revenue
        FROM bookings
        WHERE status IN ('ACTIVE','DONE')
        AND strftime('%Y', start_time) = ?
        GROUP BY month
        ORDER BY month ASC
    """;

        return executeRevenueQuery(sql, year);
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
    public List<BookingDTO> getExpiredBookings(LocalDateTime now) {
        List<BookingDTO> list = new ArrayList<>();
        String sql = """
        SELECT b.id, u.email, s.slot_code, b.slot_id
        FROM bookings b
        JOIN users u ON b.user_id = u.id
        JOIN parking_slots s ON b.slot_id = s.id
        WHERE b.status = 'DONE'
        AND b.mail_sent = 0
    """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                BookingDTO dto = new BookingDTO();
                dto.setId(rs.getInt("id"));
                dto.setEmail(rs.getString("email"));
                dto.setSlotCode(rs.getString("slot_code"));
                dto.setSlotId(rs.getInt("slot_id"));
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void markMailSent(int bookingId) {
        String sql = "UPDATE bookings SET mail_sent = 1 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}