package msa.messagequeue;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class RPCClient {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";
    private String replyQueueName;

    public RPCClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        connection = factory.newConnection();
        channel = connection.createChannel();

        replyQueueName = channel.queueDeclare().getQueue();
    }

    public String call(String message) throws IOException, InterruptedException {
        String corrId = UUID.randomUUID().toString();

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
        RPCClient Rpc = null;
        String response = "";



        try {
            Rpc = new RPCClient();

            System.out.println(" [x] Requesting JSON");

            JSONObject jsonString = new JSONObject();
            JSONObject jsonStringInner = new JSONObject();

            //jsonStringInner.put("id","8d86fbfa-b98c-47d0-aeef-ee7a3450a06d");
            jsonStringInner.put("password","password");
            jsonStringInner.put("email","jojo@gmail.com");

               /* jsonStringInner.put("age","14");
                jsonStringInner.put("firstName","Moe");
                jsonStringInner.put("lastName","Moe");
                jsonStringInner.put("gender","true");
                jsonStringInner.put("username","MoeUserName");*/


            //////   ******Blocking/Unblocking users Test*****
//                jsonString.put("method", "blockUser");
//                        jsonString.put("payload" ,jsonStringInner);
//                jsonString.put("otherUserId", "7dc85920-48e1-4b6c-a976-c3a3d8cbdd52");

//                jsonString.put("method", "unblockUser");
//                jsonString.put("payload" ,jsonStringInner);
//                jsonString.put("otherUserId", "7dc85920-48e1-4b6c-a976-c3a3d8cbdd52");

            //////   ****** Follow/Unfollow Hashtag Test*****
              jsonString.put("method", "signIn");
                jsonString.put("payload" ,jsonStringInner);
                //jsonString.put("hashtagId", "7dc85920-48e1-4b6c-a976-c3a3d8cbdd52");
//
//                jsonString.put("method", "unfollowHashtags");
//                jsonString.put("payload" ,jsonStringInner);
//                jsonString.put("hashtagId", "7dc85920-48e1-4b6c-a976-c3a3d8cbdd52");
//
            ////////****** Follow/Unfollow Category test **********
                //jsonString.put("method", "followCategories");
               // jsonString.put("payload" ,jsonStringInner);
                //jsonString.put("categoryId", "7dc85920-48e1-4b6c-a976-c3a3d8cbdd52");
//
//                jsonString.put("method", "unfollowCategories");
//                jsonString.put("payload" ,jsonStringInner);
//                jsonString.put("categoryId", "7dc85920-48e1-4b6c-a976-c3a3d8cbdd52");


            ///////////////////// ***** Add board test *****************
//                jsonString.put("method", "addBoard");
//                jsonString.put("payload" ,jsonStringInner);
//                jsonString.put("boardId", "7dc85920-48e1-4b6c-a976-c3a3d8cbdd52");
//                jsonString.put("method", "removeBoard");
//                jsonString.put("payload" ,jsonStringInner);
//                jsonString.put("boardId", "7dc85920-48e1-4b6c-a976-c3a3d8cbdd52");

            response = Rpc.call(jsonString.toString());
            System.out.println(" [.] Got '" + response + "'");
        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (Rpc != null) {
                try {
                    Rpc.close();
                } catch (IOException _ignore) {
                }
            }
        }
    }

}

