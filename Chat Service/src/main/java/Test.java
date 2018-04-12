package Commands;

import com.google.gson.JsonArray;
import com.rabbitmq.client.*;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class Test {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "chat";
    private String replyQueueName = "chat-response";

    public Test() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();

        //replyQueueName = channel.queueDeclare().getQueue();
    }

    public String call(String message) throws IOException, InterruptedException {
        final String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

        final BlockingQueue<String> response = new ArrayBlockingQueue<String>(1);

        channel.basicConsume(replyQueueName, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    response.offer(new String(body, "UTF-8"));
                }
            }
        });

        return response.take();
    }

    public void close() throws IOException {
        connection.close();
    }

    public static void main(String[] argv) {
            Test rpcClient = null;
            String response = null;

            try {

                rpcClient = new Test();
                JSONObject jsonString = new JSONObject();
                JSONObject jsonStringInner = new JSONObject();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("command", "UpdateCategory");
                jsonStringInner.put("title", "dresses");
                JsonArray posts_id = new JsonArray();
                posts_id.add("2");
                jsonStringInner.put("posts_id", posts_id);

                jsonObject.put("category_object", jsonStringInner);
                jsonObject.put("category_id", "61316");

                response = rpcClient.call("{\"command\" : \"GetCategory\",\"category_id\" : \"61316\"}");
                System.out.println(" [.] Got '" + response + "'");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (rpcClient != null) {
                    try {
                        rpcClient.close();
                    } catch (IOException _ignore) {
                    }
                }
            }
        }

}

