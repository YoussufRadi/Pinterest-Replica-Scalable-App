import Arango.PostDBObject;
import LiveObjects.PostLiveObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;



public class updatePost extends Command {

    @Override
    protected void execute() {
        HashMap<String, Object> parameters = data;


        Channel channel = (Channel) parameters.get("channel");

        AMQP.BasicProperties properties = (AMQP.BasicProperties) parameters.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) parameters.get("replyProps");
        Envelope envelope = (Envelope) parameters.get("envelope");
        JsonParser jsonParser = new JsonParser();
        System.out.println(properties.getReplyTo());

        JsonObject jsonObject = (JsonObject) jsonParser.parse((String) parameters.get("body"));
        Gson gson = new GsonBuilder().create();
        Message message = gson.fromJson((String) parameters.get("body"), Message.class);
        String post = gson.toJson(update_post(message.getPost_id(),message.getPost_object()));
        String response = post;
        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
            //System.out.println(envelope.getDeliveryTag());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public PostDBObject update_post(String post_id,PostDBObject postDBObject){
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