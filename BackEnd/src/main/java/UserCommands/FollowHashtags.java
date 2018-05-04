package UserCommands;

import Interface.ConcreteCommand;
import org.json.JSONObject;

import java.util.UUID;

public class FollowHashtags extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.followHashtags(message.getPayload().getId(), UUID.fromString(message.getHashtagId()));

        String res = respBool + "";
        JSONObject response  = new JSONObject();
        response.put("success",res);
        responseJson = jsonParser.parse(response.toString());
    }
}
