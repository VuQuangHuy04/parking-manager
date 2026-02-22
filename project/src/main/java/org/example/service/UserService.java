package org.example.service;

import org.example.api.osmAPI;
import org.example.dao.UserDao;
import org.example.dao.UserDaoimpl;
import org.example.model.User;
import org.example.session.UserSession;

public class UserService {
    private UserDaoimpl userDao = new UserDaoimpl();
    public boolean updateUserAddress(int userId, String address) {
        Double[] coordinate = osmAPI.getCoordinateFromAddress(address);
        if (coordinate == null) {
            System.out.println("Không tìm được tọa độ từ địa chỉ");
            return false;
        }
        boolean updated = userDao.updateUserLocation(userId, coordinate[0], coordinate[1]);
        if (updated) {
            UserSession.setUserLat(coordinate[0]);
            UserSession.setUserLon(coordinate[1]);
        }
        return updated;
    }
    public boolean updateEmail(int userId, String email) {
        return userDao.updateEmailIfEmpty(userId, email);
    }
    public User getCurrentUser(int userId){
        return userDao.getCurrentUser(userId);
    }
}
