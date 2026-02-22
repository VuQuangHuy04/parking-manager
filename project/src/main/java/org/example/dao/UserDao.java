package org.example.dao;

import org.example.constant.AuthResult;
import org.example.model.User;

public interface UserDao {
    public User findByUsername(String username);
    public AuthResult insertUser(String name, String password);
    public boolean updateUserLocation(int userId, double lat, double lon);
    public boolean updateEmailIfEmpty(int userId, String newEmail);
}
