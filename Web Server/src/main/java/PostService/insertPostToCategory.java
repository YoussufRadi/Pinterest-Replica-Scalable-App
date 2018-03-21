package PostService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.HashMap;


public class insertPostToCategory extends Command {

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
        Message message = gson.fromJson((String) jsonObject.get("body").toString(), Message.class);


        String category = gson.toJson(insertPostToCategory(message.getPost_id(),message.getCategory_id()));
        jsonObject.add("response",jsonParser.parse(category));
        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, jsonObject.toString().getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
            //System.out.println(envelope.getDeliveryTag());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public CategoryDBObject insertPostToCategory(String post_id,String category_id){
        arangoInstance.addNewPostToCategory(category_id,post_id);
        CategoryDBObject categoryDBObject = arangoInstance.getCategory(category_id);
        CategoryLiveObject categoryLiveObject = liveObjectService.get(CategoryLiveObject.class,category_id);
        if(categoryLiveObject != null){
            categoryLiveObject.setPosts_id(categoryDBObject.getPosts_id());
        }
        return categoryDBObject;

    }
}