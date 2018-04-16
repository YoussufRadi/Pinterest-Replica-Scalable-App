package UserCommands;

import Interface.ConcreteCommand;
import Models.User;

public class UpdateUser extends ConcreteCommand {

    @Override
    protected void doCommand() {

        User payload = message.getPayload();

        boolean respBol = UserCacheController.updateUser(payload.getId()
                , payload.getFirstName(),
                payload.getLastName(), payload.getPassword(),
                payload.getUsername(), payload.getAge(), payload.isGender());

        String response = respBol + "";
        responseJson = jsonParser.parse(response);
    }

}
