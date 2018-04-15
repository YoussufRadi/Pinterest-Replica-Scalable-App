package PostCommands;

import Database.ArangoInstance;
import Interface.Command;
import Models.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import org.redisson.api.RLiveObjectService;

import java.io.IOException;
import java.util.HashMap;


public class GetPostsOfTag extends Command {

    @Override
    protected void execute() {
        HashMap<String, Object> parameters = data;


        Channel channel = (Channel) parameters.get("channel");

        ArangoInstance arangoInstance = (ArangoInstance)
                parameters.get("ArangoInstance");

        AMQP.BasicProperties properties = (AMQP.BasicProperties) parameters.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) parameters.get("replyProps");
        Envelope envelope = (Envelope) parameters.get("envelope");
        JsonParser jsonParser = new JsonParser();
        System.out.println(properties.getReplyTo());

        JsonObject jsonObject = (JsonObject)jsonParser.parse((String) parameters.get("body"));
        Gson gson = new GsonBuilder().create();
        Message message = gson.fromJson((String) jsonObject.get("body").toString(), Message.class);
        //System.out.println();
        String posts = gson.toJson(arangoInstance.getPostsOfTagLimit(message.getSkip(),message.getLimit(),message.getTag_name()));

        jsonObject.add("response",jsonParser.parse(posts));

        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, jsonObject.toString().getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
            //System.out.println(envelope.getDeliveryTag());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
