package UserCommands;

import Interface.ConcreteCommand;
import org.json.JSONObject;

import java.util.UUID;

public class UnFollowUser extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.unfollowUser(message.getPayload().getId(), UUID.fromString(message.getOtherUserId()));

        String res = respBool + "";
        JSONObject response  = new JSONObject();
        response.put("success",res);
        responseJson = jsonParser.parse(response.toString());
    }
}
