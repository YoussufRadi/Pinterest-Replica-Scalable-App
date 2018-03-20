import Arango.CategoryDBObject;
import Arango.PostDBObject;
import LiveObjects.CategoryLiveObject;
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



public class updateCategory extends Command {

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
        System.out.println(jsonObject);
        Gson gson = new GsonBuilder().create();
        Message message = gson.fromJson((String) parameters.get("body"), Message.class);
        String category = gson.toJson(updateCategory(message.getCategory_id(),message.getCategory_object()));
        String response = category;
        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
            //System.out.println(envelope.getDeliveryTag());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public CategoryDBObject updateCategory(String category_id, CategoryDBObject categoryDBObject){
        arangoInstance.updateCategory(category_id,categoryDBObject);
        CategoryLiveObject categoryLiveObject = liveObjectService.get(CategoryLiveObject.class,category_id);
        if(categoryLiveObject != null){
            categoryLiveObject.setTitle(categoryDBObject.getTitle());
            categoryLiveObject.setPosts_id(categoryDBObject.getPosts_id());
        }
        return arangoInstance.getCategory(category_id);
    }
}