package UserCommands;

import Interface.ConcreteCommand;
import Models.NotificationDBObject;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.util.UUID;

public class FollowUser extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.followUser(UUID.fromString(message.getUser_id()), UUID.fromString(message.getOtherUserId()));

        String res = respBool + "";
        if(respBool){
            NotificationDBObject notificationDBObject = new NotificationDBObject(message.getOtherUserId(),
                    message.getUser_id(),message.getPayload().getUsername()+" started following you");
            ArangoInstance.insertNewNotification(notificationDBObject);
        }
        JSONObject response  = new JSONObject();
        response.put("success",res);
        responseJson = jsonParser.parse(response.toString());
        System.out.println(response);
    }
}
