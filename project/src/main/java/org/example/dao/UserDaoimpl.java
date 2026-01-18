package org.example.dao;

import org.example.constant.AuthResult;
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
    @Override
    public AuthResult insertUser(String username, String password){
        try (Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement("select * from users where username = ?");
            ps.setString(1,username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {return AuthResult.USERNAME_EXISTS;}
            else{
                PreparedStatement insert = conn.prepareStatement("insert into users(username,password,role) values (?,?,?)");
                insert.setString(1,username);
                insert.setString(2,password);
                insert.setString(3,"USER");
                insert.executeUpdate();
                return AuthResult.SUCCESS;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return AuthResult.ERROR;
    }
}
