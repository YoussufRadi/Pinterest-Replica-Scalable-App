package PostCommands;


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
import org.json.JSONObject;
import org.redisson.api.RLiveObjectService;

import java.io.IOException;
import java.util.HashMap;


public class UpdatePost extends Command {

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

        String s  = "";
        if(message.getPost_id() == null){
            s+= "post_id is missing";
        }
        if(message.getPost_object()==null){
            s+= "post_object is missing";
        }
        if(s.isEmpty()) {
            String post = gson.toJson(update_post(message.getPost_id(), message.getPost_object(), ArangoInstance, RLiveObjectService));
            jsonObject.add("response",jsonParser.parse(post));
        }else {
            System.out.println(s);
            JSONObject err = new JSONObject();
            err.put("error",s);
            jsonObject.add("response",jsonParser.parse(err.toString()));
        }

        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, jsonObject.toString().getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
            //System.out.println(envelope.getDeliveryTag());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public PostDBObject update_post(String post_id, PostDBObject postDBObject, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        arangoInstance.updatePost(post_id,postDBObject);
        PostLiveObject postLiveObject = liveObjectService.get(PostLiveObject.class,post_id);
        if (postLiveObject!= null){
            postLiveObject.setCategories_id(postDBObject.getCategories_id());
            postLiveObject.setComments_id(postDBObject.getComments_id());
            postLiveObject.setDislikes_id(postDBObject.getDislikes_id());
            postLiveObject.setImage_id(postDBObject.getImage_id());
            postLiveObject.setUser_id(postDBObject.getUser_id());
            postLiveObject.setTags_id(postDBObject.getTags_id());
        }
        return arangoInstance.getPost(post_id);
    }
}