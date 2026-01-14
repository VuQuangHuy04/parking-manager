package org.example.service;

import org.example.dao.UserDaoimpl;
import org.example.model.User;

public class AuthService {
    private UserDaoimpl userDaoimpl = new UserDaoimpl();
    public User login(String username, String password){
        User user = userDaoimpl.findByUsername(username);
        if(user.getUsername().equals(username) && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }
}
