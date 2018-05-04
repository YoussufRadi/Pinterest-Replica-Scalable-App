package UserCommands;

import Interface.ConcreteCommand;
import Models.User;
import org.json.JSONObject;

public class UpdateUser extends ConcreteCommand {

    @Override
    protected void doCommand() {

        User payload = message.getPayload();

        boolean respBol = UserCacheController.updateUser(payload.getId()
                , payload.getFirstName(),
                payload.getLastName(), payload.getPassword(),
                payload.getUsername(), payload.getAge(), payload.isGender());

        String res = respBol + "";
        JSONObject response  = new JSONObject();
        response.put("success",res);
        responseJson = jsonParser.parse(response.toString());
    }

}
