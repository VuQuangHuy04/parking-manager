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
    public static Double getUserlat(){return currentUser != null ? currentUser.getLat() : null;}
    public static Double getUserlon(){return currentUser != null ? currentUser.getLon() : null;}
    public static void clear() {
        currentUser = null;
    }
}
