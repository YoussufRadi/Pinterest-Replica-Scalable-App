package UserCommands;

import Interface.ConcreteCommand;

import java.util.UUID;

public class UnlikePhoto extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.unlikePhotos(message.getPayload().getId(), UUID.fromString(message.getPhotoId()));

        String response = respBool + "";
        responseJson = jsonParser.parse(response);
        System.out.println(response);
    }
}
