package Commands;


import Models.CategoryLiveObject;
import Models.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.HashMap;


public class DeleteCategory extends Command {

    @Override
    protected void execute() {
        HashMap<String, Object> parameters = data;


        Channel channel = (Channel) parameters.get("channel");

        AMQP.BasicProperties properties = (AMQP.BasicProperties) parameters.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) parameters.get("replyProps");
        Envelope envelope = (Envelope) parameters.get("envelope");
        JsonParser jsonParser = new JsonParser();
        System.out.println(properties.getReplyTo());

        JsonObject jsonResponse = (JsonObject) jsonParser.parse((String) parameters.get("body"));

        Gson gson = new GsonBuilder().create();
        Message message = gson.fromJson((String) jsonResponse.get("body").toString(), Message.class);

        deleteCategory(message.getCategory_id());
        //System.out.println("Ready to send   :   "  + jsonResponse);
        jsonResponse.add("response", new JsonObject());
        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, jsonResponse.toString().getBytes("UTF-8"));
            channel.basicAck(envelope.getDeliveryTag(), false);
            //System.out.println(envelope.getDeliveryTag());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void deleteCategory(String category_id){
        arangoInstance.deleteCategory(category_id);
        CategoryLiveObject categoryLiveObject = liveObjectService.get(CategoryLiveObject.class,category_id);
        if(categoryLiveObject!=null){
            liveObjectService.delete(CategoryLiveObject.class,category_id);
        }
    }
}