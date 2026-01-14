package org.example.dao;

import org.example.model.User;

public interface UserDao {
    public User findByUsername(String username);

}
