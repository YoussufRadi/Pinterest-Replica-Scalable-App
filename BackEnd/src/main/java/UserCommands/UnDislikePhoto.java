package UserCommands;

import Interface.ConcreteCommand;

import java.util.UUID;

public class UnDislikePhoto extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.undislikePhotos(message.getPayload().getId(), UUID.fromString(message.getPhotoId()));

        String response = respBool + "";
        responseJson = jsonParser.parse(response);
        System.out.println(response);
    }
}
