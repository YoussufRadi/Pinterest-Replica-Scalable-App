package UserCommands;

import Interface.ConcreteCommand;
import Models.User;

import java.util.UUID;

public class SignUp extends ConcreteCommand {

    @Override
    protected void doCommand() {
        User payload = message.getPayload();

        UUID id = UserCacheController.addUser(payload.getFirstName(),
                payload.getLastName(), payload.getUsername(),
                payload.getEmail(), payload.getPassword(), payload.isGender(), payload.getAge());

        String response = id.toString();
        responseJson = jsonParser.parse(response);
        System.out.println(response);
    }
}
