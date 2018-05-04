package UserCommands;

import Interface.ConcreteCommand;
import org.json.JSONObject;

import java.util.UUID;

public class RemovePin extends ConcreteCommand {

    @Override
    protected void doCommand() {

        boolean respBool =
                UserCacheController.removePin(message.getPayload().getId(), UUID.fromString(message.getPinId()));

        String res = respBool + "";
        JSONObject response  = new JSONObject();
        response.put("success",res);
        responseJson = jsonParser.parse(response.toString());
    }
}
