package PostService.java;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.HashMap;


public class getPost extends Command {

    @Override
    protected void execute() {
        HashMap<String, Object> parameters = data;


        Channel channel = (Channel) parameters.get("channel");

        AMQP.BasicProperties properties = (AMQP.BasicProperties) parameters.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) parameters.get("replyProps");
        Envelope envelope = (Envelope) parameters.get("envelope");
        JsonParser jsonParser = new JsonParser();
        System.out.println(properties.getReplyTo());

        JsonObject jsonObject = (JsonObject)jsonParser.parse((String) parameters.get("body"));
        Gson gson = new GsonBuilder().create();
        Message message = gson.fromJson((String) parameters.get("body"),Message.class);
        String post = gson.toJson(getPost(message.getPost_id()));
        String response = post;
        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
            //System.out.println(envelope.getDeliveryTag());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public PostLiveObject getPost(String post_id){
        PostLiveObject postLiveObject = liveObjectService.get(PostLiveObject.class,post_id);
        System.out.println(postLiveObject);
        if(postLiveObject==null){
            PostDBObject postDBObject= arangoInstance.getPost(post_id);
            if(postDBObject!=null) {
                String message = new Gson().toJson(postDBObject);
                Gson gson = new GsonBuilder().create();
                System.out.println(message);
                postLiveObject = gson.fromJson(message, PostLiveObject.class);
                postLiveObject = liveObjectService.merge(postLiveObject);

            }
        }
        return postLiveObject;

    }

}
