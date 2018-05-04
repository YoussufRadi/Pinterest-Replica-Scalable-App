package Models;

import com.arangodb.entity.DocumentEntity;

public class NotificationDBObject extends DocumentEntity {
    private String user_id;
    private String user_following_name;
    private String message;

    public NotificationDBObject(String user_id,String user_following_name,String message){
        this.user_id  = user_id;
        this.user_following_name = user_following_name;
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_following_name() {
        return user_following_name;
    }

    public void setUser_following_name(String user_following_name) {
        this.user_following_name = user_following_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
