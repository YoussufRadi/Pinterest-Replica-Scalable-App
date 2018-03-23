package PostService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class updateComment extends Command {
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

        String s  = "";
        if(message.getComment_id() == null){
            s+= "Comment_id is missing";
        }
        if(message.getComment_object()==null){
            s+= "Comment_object is missing";
        }
        if(s.isEmpty()) {
            String comment = gson.toJson(update_comment(message.getComment_id(), message.getComment_object()));
            jsonObject.add("response",jsonParser.parse(comment));
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
    public CommentDBObject update_comment(String comment_id, CommentDBObject commentDBObject){
        arangoInstance.updateComment(comment_id,commentDBObject);
        return arangoInstance.getComment(comment_id);
    }
}
