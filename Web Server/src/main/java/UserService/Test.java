package UserService;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Test {

    public static void main(String args[]){
        //This is used to send a message to this queue service and execute the getUser Command;
        final String requestId = "0111111222";
        sendMessage("user", requestId, "{\"command\" : \"GetUser\"}");
    }

    //Just for testing
    private static void sendMessage(String service, String requestId, String message){
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel mqChannel = connection.createChannel();
            mqChannel.queueDeclare(service + "-request", false, false, false, null);

            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(requestId)
                    .replyTo(service + "-response")
                    .build();
            System.out.println("Sent: "+ message);
            mqChannel.basicPublish("", service + "-request", props, message.getBytes());
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
