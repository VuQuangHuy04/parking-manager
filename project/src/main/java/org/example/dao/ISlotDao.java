package org.example.dao;

import org.example.model.ParkingSlot;

import java.time.LocalDateTime;
import java.util.List;

public interface ISlotDao {
    public List<ParkingSlot> getAllSlots();
    public List<ParkingSlot> getSlotsByLotId(int lotId);
    public boolean insertSlot(ParkingSlot slot);
    public void updateSlotPosition(int lotId, String code, double x, double y);
    public boolean deleteSlot(int slotId);
    public boolean updateStatus(int slotId, String status, int userId);
}
