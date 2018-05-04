package TestService;

import Interface.Command;
import Models.Message;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import org.json.JSONObject;

import java.io.IOException;
import java.util.TreeMap;

public class GetUserCommand extends Command {

    public void execute() {

//        TreeMap<String, Object> parameters = data;
//
//        Channel channel = (Channel) parameters.get("channel");
//
//        AMQP.BasicProperties properties = (AMQP.BasicProperties) parameters.get("properties");
//        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) parameters.get("replyProps");
//        Envelope envelope = (Envelope) parameters.get("envelope");
//
//        JSONObject request = (JSONObject) data.get("request");
//        JSONObject body = (JSONObject) request.get("body");
//        System.out.println("Body :  " + body);
//        String response = UserModel.getUser((String) body.get("text"));
//        System.out.println("Response :  " + response);
//        System.out.println(properties.getReplyTo());
//        System.out.println();
//        request.put("response", response);
//        try {
//            channel.basicPublish("", properties.getReplyTo(), replyProps, request.toString().getBytes("UTF-8"));
//            channel.basicAck(envelope.getDeliveryTag(), false);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public void setMessage(Message message) {
        return ;
    }

    @Override
    public Message getMessage() {
        return null;
    }
}
