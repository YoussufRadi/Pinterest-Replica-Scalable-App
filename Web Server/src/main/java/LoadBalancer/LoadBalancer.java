package LoadBalancer;

import UserService.Command;
import com.rabbitmq.client.*;
import io.netty.util.concurrent.Future;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

public class LoadBalancer {
    private static final String HOST = "localhost";
    private static final String RPC_QUEUE_NAME = "load_balancer";
    private static final String POST_QUEUE_NAME = "post";
    private static final String USER_QUEUE_NAME = "user";
    private static final String CHAT_QUEUE_NAME = "chat";

//    static ExecutorService executorService = Executors.newFixedThreadPool(15);

    private final HashMap<String, Channel> REQUEST_CHANNEL_MAP = new HashMap<String, Channel>();


    public LoadBalancer() {

        establishConnections();

        try {

            Channel balancer = REQUEST_CHANNEL_MAP.get(RPC_QUEUE_NAME);

            System.out.println(" [x] Awaiting RPC requests");

            Consumer consumer = new DefaultConsumer(balancer) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        //Using Reflection to convert a command String to its appropriate class
                        String message = new String(body, "UTF-8");
                        JSONObject jsonRequest = new JSONObject(message);

                        String appName = (String) jsonRequest.get("application");
                        Channel receiver = REQUEST_CHANNEL_MAP.get(appName);
                        System.out.println("Request    :   " + message);
                        System.out.println("Application    :   " + appName);
                        System.out.println();

                        receiver.basicPublish("", appName, properties, body);

                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        synchronized (this) {
                            this.notify();
                        }
                    }

                }
            };


            balancer.basicConsume(RPC_QUEUE_NAME, true, consumer);
            // Wait and be prepared to consume the message from RPC client.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            if (connection != null)
//                try {
//                    //connection.close();
//                } catch (IOException _ignore) {}
        }
    }

    public static void main(String[] argv) {
        new LoadBalancer();
    }

    private void establishConnections() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = null;
        try {
            connection = factory.newConnection();
            final Channel loadBalancerChannel = connection.createChannel();
            final Channel postChannel = connection.createChannel();
            final Channel userChannel = connection.createChannel();
            final Channel chatChannel = connection.createChannel();

            loadBalancerChannel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            loadBalancerChannel.basicQos(1);
            userChannel.queueDeclare(USER_QUEUE_NAME, true, false, false, null);
            postChannel.queueDeclare(POST_QUEUE_NAME, true, false, false, null);
            chatChannel.queueDeclare(CHAT_QUEUE_NAME, true, false, false, null);

            REQUEST_CHANNEL_MAP.put(RPC_QUEUE_NAME, loadBalancerChannel);
            REQUEST_CHANNEL_MAP.put(USER_QUEUE_NAME, userChannel);
            REQUEST_CHANNEL_MAP.put(POST_QUEUE_NAME, postChannel);
            REQUEST_CHANNEL_MAP.put(CHAT_QUEUE_NAME, chatChannel);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
