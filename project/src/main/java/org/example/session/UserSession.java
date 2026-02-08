package org.example.session;

import org.example.model.User;

public class UserSession {
    private static User currentUser;
    private UserSession() {
    }
    public static void setUser(User user) {
        currentUser = user;
    }
    public static User getUser() {
        return currentUser;
    }
    public static int getUserId() {
        return currentUser != null ? currentUser.getUser_id() : null;
    }
    public static Double getUserLat(){return currentUser != null ? currentUser.getLat() : null;}
    public static Double getUserLon(){return currentUser != null ? currentUser.getLon() : null;}
    public static void clear() {
        currentUser = null;
    }
    public static void setUserLat(double lat) {
        if (currentUser != null) {
            currentUser.setLat(lat);
        }
    }
    public static void setUserLon(double lon) {
        if (currentUser != null) {
            currentUser.setLon(lon);
        }
    }
    public static void GetUserRole() {
        if (currentUser != null) {
            currentUser.getRole();
        }
    }
    public static  void logout(){
        currentUser = null;
    }
}
