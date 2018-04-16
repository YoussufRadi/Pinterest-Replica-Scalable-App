package UserCommands;

import Interface.ConcreteCommand;
import Models.User;
import com.google.gson.Gson;

public class SignIn extends ConcreteCommand {

    @Override
    protected void doCommand() {
        User payload = message.getPayload();

        User userLiveObject = UserCacheController.signIn(payload.getEmail(),
                payload.getPassword());

        String response = new Gson().toJson(userLiveObject);
        responseJson = jsonParser.parse(response);
        System.out.println(response+"SigInResonse");
    }
}
