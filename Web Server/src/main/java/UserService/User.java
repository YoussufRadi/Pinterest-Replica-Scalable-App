package UserService;


import org.json.simple.JSONObject;

import java.sql.*;
import java.util.Properties;

public class User {
    //example
    public static String getUser() {
        JSONObject userObject = new JSONObject();
        userObject.put("user_name","test");
        userObject.put("email","test@test.com");
        userObject.put("date_of_birth","1/1/2001");
        userObject.put("gender","true");
        return userObject.toString();
    }
}
