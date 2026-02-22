package org.example.model;

public class User {
    private int user_id;
    private String username;
    private String password;
    private String role;
    private double lat;
    private double lon;
    private String mail;
    public User(){
    }
    public User( int user_id, String username, String password, String role, String mail) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.mail = mail;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
}
