
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class RPCClient {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "post_queue";
    private String replyQueueName;

    public RPCClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();

        replyQueueName = channel.queueDeclare().getQueue();
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
        RPCClient rpcClient = null;
        String response = null;

        try {
            rpcClient = new RPCClient();
            JSONObject jsonString = new JSONObject();
            JSONObject jsonStringInner = new JSONObject();
            JSONArray likes_id = new JSONArray();
            likes_id.add("1");
            JSONArray dislikes_id = new JSONArray();
            dislikes_id.add("1");
            JSONArray comments_id = new JSONArray();
            comments_id.add("1");
            JSONArray categories_id = new JSONArray();
            categories_id.add("1");
            JSONArray tags_id = new JSONArray();
            tags_id.add("1");

            jsonStringInner.put("user_id","jojo@gmail.com");
            jsonStringInner.put("likes_id",likes_id);
            jsonStringInner.put("dislikes_id",dislikes_id);
            jsonStringInner.put("comments_id",comments_id);
            jsonStringInner.put("categories_id",categories_id);
            jsonStringInner.put("tags_id",tags_id);
            jsonStringInner.put("image_id","4");

            jsonString.put("method", "insert_post");
            jsonString.put("payload" ,jsonStringInner);

            System.out.println(" [x] add post ");
            response = rpcClient.call(jsonString.toString());
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


