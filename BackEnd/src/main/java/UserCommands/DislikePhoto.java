package UserCommands;

import Interface.ConcreteCommand;
import org.json.JSONObject;

import java.util.UUID;

public class DislikePhoto extends ConcreteCommand {
    @Override
    protected void doCommand() {

        boolean respBool =
                UserCacheController.dislikePhotos(message.getPayload().getId(), UUID.fromString(message.getPhotoId()));

        String res = respBool + "";
        JSONObject response  = new JSONObject();
        response.put("success",res);
        responseJson = jsonParser.parse(response.toString());
    }
}
