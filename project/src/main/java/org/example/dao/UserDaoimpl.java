package org.example.dao;

import org.example.model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.example.utils.DBConnection;
public class UserDaoimpl implements UserDao{
    @Override
    public User findByUsername(String username){
        try (Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement("select * from users where username = ?");
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) return new User(rs.getInt("id"),rs.getString("username"),rs.getString("password"),rs.getString("role"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
