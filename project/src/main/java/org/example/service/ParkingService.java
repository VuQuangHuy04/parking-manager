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
        return slotDaoimpl.getAllSlots();
    }
    public boolean insertSlot(ParkingSlot slot) {
        return slotDaoimpl.insertSlot(slot);
    }
    public boolean deleteSlot(int slotId) {
        return slotDaoimpl.deleteSlot(slotId);
    }
    public List<ParkingSlot> getSlots(int lotId) {
        return slotDaoimpl.getSlotsByLotId(lotId);
    }
    public boolean bookSlot(int slotId, int userId, int hours, String paymentMethod) {
        return bookingDAO.createBooking(slotId, userId, hours, paymentMethod);
    }
    public double calculatePrice(double minutes) {
        double pricePerMinute = 5000.0 / 60.0;
        return Math.ceil(minutes * pricePerMinute);
    }
    public boolean confirmPayment(int slotId) {
        return bookingDAO.confirmPayment(slotId);
    }
    public LocalDateTime getBookingEndTime(int slotId) {
        return bookingDAO.getActiveBookingEndTime(slotId);
    }
    public void updateSlotPositionp(int lotId, String code, double x, double y) {
        slotDaoimpl.updateSlotPosition(lotId, code, x, y);
    }

}