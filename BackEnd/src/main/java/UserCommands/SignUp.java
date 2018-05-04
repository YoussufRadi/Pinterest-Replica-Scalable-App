package UserCommands;

import Interface.ConcreteCommand;
import Models.User;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import java.util.UUID;

public class SignUp extends ConcreteCommand {

    @Override
    protected void doCommand() {
        User payload = message.getPayload();

        UUID id = UserCacheController.addUser(payload.getFirstName(),
                payload.getLastName(), payload.getUsername(),
                payload.getEmail(), payload.getPassword(), payload.isGender(), payload.getAge());

        JSONObject response = new JSONObject();
        response.put("id",id.toString());
        User user =  UserCacheController.ge
        responseJson = jsonParser.parse(response.toString());
        System.out.println(response);
    }
}
