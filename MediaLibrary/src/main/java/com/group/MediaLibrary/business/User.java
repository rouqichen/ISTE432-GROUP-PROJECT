package com.group.MediaLibrary.business;

import com.group.MediaLibrary.data.DataLayerException;
import com.group.MediaLibrary.data.UserDAO;

public class User {

    public static String login(String username, String password) {
        UserDAO dao = new UserDAO();
        int uid;
        try {
            uid = dao.login(username, password);
        } catch (DataLayerException dle) {
            return "\"error\":\"A server error has occured attempting to log in\"";
        }

        if (uid == -1) {
            return "\"error\":\"Invalid username and/or password\"";
        }

        return "\"loginToken\":" + uid;
    }

    public static String register(String username, String password) {
        UserDAO dao = new UserDAO();

        try {
            if(dao.register(username, password)) {
                return "\"success\":\"User has successfully registered\"";
            }
        } catch (DataLayerException dle) {

        }

        return "\"error\":\"The username is taken, or an error has occured\"";
    }

}
