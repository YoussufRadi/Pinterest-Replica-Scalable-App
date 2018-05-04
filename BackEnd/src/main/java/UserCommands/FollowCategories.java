package UserCommands;

import Interface.ConcreteCommand;
import org.json.JSONObject;

import java.util.UUID;

public class FollowCategories extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.followCategories(message.getPayload().getId(), UUID.fromString(message.getCategoryId()));

        String res = respBool + "";
        JSONObject response  = new JSONObject();
        response.put("success",res);
        responseJson = jsonParser.parse(response.toString());
    }
}
