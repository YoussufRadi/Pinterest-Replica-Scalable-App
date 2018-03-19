package PostService;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class test {

    public static void main(String args[]){
        //This is used to send a message to this queue service and execute the getUser Command;
        final String requestId = "0111111222";
        sendMessage("post", requestId, "{\"command\" : \"getPost\",\"post_id\" : \"32035\"}");
    }

    //Just for testing
    private static void sendMessage(String service, final String requestId, String message){
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
            final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);
            mqChannel.basicConsume("post-response", true, new DefaultConsumer(mqChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    if (properties.getCorrelationId().equals(requestId)) {
                        response.offer(new String(body, "UTF-8"));
                    }
                }
            });
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
