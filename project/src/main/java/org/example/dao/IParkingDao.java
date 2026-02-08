package org.example.dao;

import org.example.DTO.ParkingLotDTO;

import java.util.ArrayList;
import java.util.List;

public interface IParkingDao {
    public List<ParkingLotDTO> getParkingWithoutCoordinate();
    public void updateLatLonById(int id, double lat, double lon);
    public List<ParkingLotDTO> getAllParkingDTO();
}
