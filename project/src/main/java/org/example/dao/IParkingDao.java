package org.example.dao;

import org.example.DTO.ParkingLotDTO;

import java.util.ArrayList;
import java.util.List;

public interface IParkingDao {
    public List<String> getAddressList();
    public boolean UpdateLatandLon(String address);
    public List<ParkingLotDTO> GetParkingDTO();
}
