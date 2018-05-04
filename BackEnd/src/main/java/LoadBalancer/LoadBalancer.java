package LoadBalancer;
import Config.Config;
import com.rabbitmq.client.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class LoadBalancer {

    private Config config = Config.getInstance();

    private final String HOST = config.getLoadBalancerQueueHost();
    private final int PORT = config.getLoadBalancerQueuePort();
    private final String USER = config.getLoadBalancerQueueUserName();
    private final String PASS = config.getLoadBalancerQueuePass();
    private final String RPC_QUEUE_NAME = config.getLoadBalancerQueueName();
    private final String LOAD_BALANCER_EXTENSION = "-" +config.getLoadBalancerQueueName();
    private final String USER_QUEUE_NAME = config.getLoadBalancerUserQueue() + LOAD_BALANCER_EXTENSION;
    private final String POST_QUEUE_NAME = config.getLoadBalancerPostQueue()+ LOAD_BALANCER_EXTENSION;
    private final String CHAT_QUEUE_NAME = config.getLoadBalancerChatQueue()+ LOAD_BALANCER_EXTENSION;

    private final HashMap<String, Channel> REQUEST_CHANNEL_MAP = new HashMap<String, Channel>();

    public LoadBalancer() {
        establishConnections();
    }

    public void start(){
        try {
            Channel balancer = REQUEST_CHANNEL_MAP.get(RPC_QUEUE_NAME);
            System.out.println(" [x] Awaiting RPC requests on Queue : " + RPC_QUEUE_NAME);
            Consumer consumer = new DefaultConsumer(balancer) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                    try {
                        //Using Reflection to convert a command String to its appropriate class
                        String message = new String(body, "UTF-8");
                        JSONObject jsonRequest = new JSONObject(message);

                        System.out.println("Responding to corrID: "+ properties.getCorrelationId() +  ", on Queue : " + RPC_QUEUE_NAME);
                        System.out.println(message);
                        String appName = (String) jsonRequest.get("application");
                        Channel receiver = REQUEST_CHANNEL_MAP.get(appName + LOAD_BALANCER_EXTENSION);
                        System.out.println("Request    :   " + message);
                        System.out.println("Application    :   " + appName);
                        System.out.println();

                        receiver.basicPublish("", appName + LOAD_BALANCER_EXTENSION, properties, body);

                    } catch (RuntimeException| IOException e) {
                        e.printStackTrace();
                        start();
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
//            start();
        }
    }

    private void establishConnections() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername(USER);
        factory.setPassword(PASS);
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
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] argv) {
//        new LoadBalancer().start();
//    }
}
