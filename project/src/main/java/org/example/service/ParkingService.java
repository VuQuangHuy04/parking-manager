package org.example.service;
import org.example.DTO.ParkingLotDTO;
import org.example.api.osmAPI;
import org.example.dao.BookingDaoimpl;
import org.example.dao.ParkingDaoimpl;
import org.example.dao.SlotDaoimpl;
import org.example.model.ParkingSlot;
import org.example.session.UserSession;
import org.example.utils.LocationUtils;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class ParkingService {
    private ParkingDaoimpl dao = new ParkingDaoimpl();
    private SlotDaoimpl slotDaoimpl = new SlotDaoimpl();
    private BookingDaoimpl bookingDAO = new BookingDaoimpl();
    public List<ParkingLotDTO> getParkingDTO() {
        List<ParkingLotDTO> needUpdate = dao.getParkingWithoutCoordinate();
        Map<String, Double[]> cache = new HashMap<>();
        for (ParkingLotDTO p : needUpdate) {
            Double[] coordinate = cache.get(p.getAddress());
            if (coordinate == null) {
                coordinate = osmAPI.getCoordinateFromAddress(p.getAddress());
                if (coordinate != null) cache.put(p.getAddress(), coordinate);
            }
            if (coordinate != null) {
                dao.updateLatLonById(p.getId(), coordinate[0], coordinate[1]);
            }
        }
        return dao.getAllParkingDTO();
    }
    public List<ParkingLotDTO> SetDistance(List<ParkingLotDTO> list) {
        Double userLat = UserSession.getUserLat();
        Double userLon = UserSession.getUserLon();
        if (userLat == null || userLon == null) return list;

        for (ParkingLotDTO p : list) {
            double distance = LocationUtils.calculateDistance(userLat, userLon, p.getLat(), p.getLon());
            p.setDistance(distance);
        }
        list.sort((a, b) -> Double.compare(a.getDistance(), b.getDistance()));
        return list;
    }
    // --- QUẢN LÝ Ô ĐỖ (SLOTS) ---
    public List<ParkingSlot> getSlotsWithLiveStatus() {
        bookingDAO.clearExpiredBookings();
        return slotDaoimpl.getAllSlots();
    }
    public boolean insertSlot(ParkingSlot slot) {
        return slotDaoimpl.insertSlot(slot);
    }
    public boolean deleteSlot(int slotId) {
        return slotDaoimpl.deleteSlot(slotId);
    }
    // Dành cho User: Lấy map và tự động cập nhật trạng thái trống/đầy theo thời gian thực
    public List<ParkingSlot> getSlotsWithLiveStatus(int lotId) {
        bookingDAO.clearExpiredBookings(); // Quét dọn DB trước khi trả dữ liệu về UI
        return slotDaoimpl.getSlotsByLotId(lotId);
    }
    public boolean bookSlot(int slotId, int userId, int hours) {
        LocalDateTime endTime = LocalDateTime.now().plusHours(hours);
        return bookingDAO.createBooking(slotId, userId, endTime);
    }
    public LocalDateTime getBookingEndTime(int slotId) {
        return bookingDAO.getActiveBookingEndTime(slotId);
    }
    public void updateSlotPositionp(int lotId, String code, double x, double y) {
        slotDaoimpl.updateSlotPosition(lotId, code, x, y);
    }
}