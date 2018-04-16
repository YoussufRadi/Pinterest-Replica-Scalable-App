package UserCommands;

import Interface.ConcreteCommand;

import java.util.UUID;

public class DislikePhoto extends ConcreteCommand {
    @Override
    protected void doCommand() {

        boolean respBool =
                UserCacheController.dislikePhotos(message.getPayload().getId(), UUID.fromString(message.getPhotoId()));

        String response = respBool + "";
        responseJson = jsonParser.parse(response);
    }
}
