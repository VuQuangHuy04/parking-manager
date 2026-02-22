package org.example.service;
import org.example.constant.AuthResult;
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
    public AuthResult Regsister(String username, String password, String repassword){
          if(!password.equals(repassword)){
              return AuthResult.PASSWORD_NOT_MATCH;
          }else{
              return userDaoimpl.insertUser(username,password);
          }
    }
}
