package Commands;

import Cache.UserCacheController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import Models.Message;
import Models.User;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class SignIn extends Command{


    public void execute() {
        HashMap<String, Object> parameters = data;


        Channel channel = (Channel) parameters.get("channel");
        UserCacheController UserCacheController = (UserCacheController)
                parameters.get("UserCacheController");

        AMQP.BasicProperties properties = (AMQP.BasicProperties) parameters.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) parameters.get("replyProps");
        Envelope envelope = (Envelope) parameters.get("envelope");

        Gson gson = new GsonBuilder().create();
        Message msg = gson.fromJson(parameters.get("body").toString(), Message.class);
        User payload = msg.getPayload();


        User userLiveObject = UserCacheController.signIn(payload.getEmail(),
                payload.getPassword());

        String response = new Gson().toJson(userLiveObject);

        JSONObject jsonObject = (JSONObject) parameters.get("body");
        jsonObject.put("response", response);

        System.out.println(response+"SigInResonse");
        System.out.println();
        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps,
                    jsonObject.toString().getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
