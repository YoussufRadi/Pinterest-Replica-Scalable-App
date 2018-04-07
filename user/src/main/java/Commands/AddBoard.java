package Commands;

import Cache.UserCacheController;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import Models.Message;
import Models.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AddBoard extends Command {


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


        boolean respBool =
                UserCacheController.addBoard(msg.getPayload().getId(), UUID.fromString(msg.getBoardId()));

        String response = respBool + "";

        JSONObject jsonObject = (JSONObject) parameters.get("body");
        jsonObject.put("response", response);

        System.out.println(response +"Responsee");
        System.out.println();
        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, jsonObject.toString().getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
