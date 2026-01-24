package org.example.service;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.example.DTO.ParkingLotDTO;
import org.example.dao.ParkingDaoimpl;
import org.example.dao.UserDaoimpl;
import org.example.session.UserSession;

import java.util.ArrayList;
import java.util.List;

public class ParkingService {
       private ParkingDaoimpl parkingDaoimpl= new ParkingDaoimpl();
       public List<ParkingLotDTO> getParkingDTO(){
           List<ParkingLotDTO> list = new ArrayList<>();
           List<String> listAdress = parkingDaoimpl.getAddressList();
           for(String i : listAdress){
               parkingDaoimpl.UpdateLatandLon(i);
           }
           return null;
       }
}
