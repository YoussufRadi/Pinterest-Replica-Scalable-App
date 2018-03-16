package UserService;

import java.io.IOException;
import java.util.HashMap;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import org.json.JSONObject;

public class GetUser extends Command {

    public void execute() {

        HashMap<String, Object> parameters = data;

        Channel channel = (Channel) parameters.get("channel");

        AMQP.BasicProperties properties = (AMQP.BasicProperties) parameters.get("properties");
        AMQP.BasicProperties replyProps = (AMQP.BasicProperties) parameters.get("replyProps");


        System.out.println("CorrelationID :  " + replyProps.getCorrelationId());
        JSONObject body = (JSONObject) data.get("body");
        System.out.println("Body :  " + body);
        String response = User.getUser((String) body.get("text"));
        System.out.println("Response :  " + response);
        System.out.println();
        try {
            channel.basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
