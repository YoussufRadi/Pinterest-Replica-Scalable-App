package Commands;

import Database.ArangoInstance;
import Interface.Command;
import Models.Message;
import Models.PostDBObject;
import Models.PostLiveObject;
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


public class DislikePost extends Command {

    @Override
    protected void execute() {
        HashMap<String, Object> parameters = data;


        Channel channel = (Channel) parameters.get("channel");

        RLiveObjectService RLiveObjectService = (RLiveObjectService)
                parameters.get("RLiveObjectService");
        ArangoInstance ArangoInstance = (ArangoInstance)
                parameters.get("ArangoInstance");

        AMQP.BasicProperties properties = (AMQP.BasicProperties) parameters.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) parameters.get("replyProps");
        Envelope envelope = (Envelope) parameters.get("envelope");
        JsonParser jsonParser = new JsonParser();
        System.out.println(properties.getReplyTo());

        JsonObject jsonObject = (JsonObject) jsonParser.parse((String) parameters.get("body"));
        Gson gson = new GsonBuilder().create();
        Message message = gson.fromJson((String) jsonObject.get("body").toString(), Message.class);

        String post = gson.toJson(dislikePost(message.getPost_id(),message.getUser_id(), ArangoInstance, RLiveObjectService));
        jsonObject.add("response",jsonParser.parse(post));


        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, jsonObject.toString().getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
            //System.out.println(envelope.getDeliveryTag());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public PostDBObject dislikePost(String post_id, String user_id, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        arangoInstance.dislikePost(user_id,post_id);
        PostLiveObject postLiveObject = liveObjectService.get(PostLiveObject.class,post_id);
        if (postLiveObject!= null){
            postLiveObject.setLikes_id(arangoInstance.getPost(post_id).getLikes_id());
            postLiveObject.setDislikes_id(arangoInstance.getPost(post_id).getDislikes_id());
        }
        return arangoInstance.getPost(post_id);
    }
}