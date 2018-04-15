package PostCommands;


import Database.ArangoInstance;
import Interface.Command;
import Models.CategoryDBObject;
import Models.CategoryLiveObject;
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


public class UpdateCategory extends Command {

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
        System.out.println(jsonObject);
        Gson gson = new GsonBuilder().create();
        Message message = gson.fromJson((String) jsonObject.get("body").toString(), Message.class);
        System.out.println("here");
        String category = gson.toJson(updateCategory(message.getCategory_id(),message.getCategory_object(), ArangoInstance, RLiveObjectService));
        if(category != null) {
            jsonObject.add("response", jsonParser.parse(category));
        }else {
            jsonObject.add("response", new JsonObject());
        }
        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, jsonObject.toString().getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
            //System.out.println(envelope.getDeliveryTag());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public CategoryDBObject updateCategory(String category_id, CategoryDBObject categoryDBObject, ArangoInstance arangoInstance, RLiveObjectService liveObjectService){
        arangoInstance.updateCategory(category_id,categoryDBObject);
        CategoryLiveObject categoryLiveObject = liveObjectService.get(CategoryLiveObject.class,category_id);
        if(categoryLiveObject != null){
            categoryLiveObject.setTitle(categoryDBObject.getTitle());
            categoryLiveObject.setPosts_id(categoryDBObject.getPosts_id());
        }
        return arangoInstance.getCategory(category_id);
    }
}