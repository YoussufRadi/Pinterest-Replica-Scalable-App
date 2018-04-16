package UserCommands;

import Interface.ConcreteCommand;

import java.util.UUID;

public class LikePhoto extends ConcreteCommand {

    @Override
    protected void doCommand() {
        boolean respBool =
                UserCacheController.likePhotos(message.getPayload().getId(),
                        UUID.fromString(message.getPhotoId()));

        String response = respBool + "";
        responseJson = jsonParser.parse(response);
        System.out.println(response);
    }
}
