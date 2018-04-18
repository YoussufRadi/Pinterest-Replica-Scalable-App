package TestService;

import Interface.Command;
import com.rabbitmq.client.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

public class UserService {

    private static final String RPC_QUEUE_NAME = "user";
    private static int threadCount = 5;

    public static void main(String [] argv) {

        //initialize thread pool of fixed size
        final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadCount);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            channel.queueDeclare(RPC_QUEUE_NAME, true, false, false, null);

            channel.basicQos(threadCount);

            System.out.println(" [x] Awaiting RPC requests on Queue : " + RPC_QUEUE_NAME);

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(properties.getCorrelationId())
                            .build();
                    System.out.println("Responding to corrID: "+ properties.getCorrelationId() +  ", on Queue : " + RPC_QUEUE_NAME);

                    try {
                        //Using Reflection to convert a command String to its appropriate class
                        String message = new String(body, "UTF-8");
                        JSONObject jsonRequest = new JSONObject(message);

                        String className = "ServiceTest." + (String)jsonRequest.get("command");
                        Class com = Class.forName(className);
                        Command cmd = (Command) com.newInstance();
                        TreeMap<String, Object> init = new TreeMap<>();
                        init.put("channel", channel);
                        init.put("properties", properties);
                        init.put("replyProps", replyProps);
                        init.put("request", jsonRequest);
                        init.put("envelope", envelope);
                        cmd.init(init);
                        executor.submit(cmd);

                    } catch (RuntimeException e) {
                        System.out.println(" [.] " + e.toString());
                    }  catch (IOException | InstantiationException | ClassNotFoundException | IllegalAccessException e) {
                        e.printStackTrace();
                    } finally {
                        synchronized (this) {
                            this.notify();
                        }
                    }
                }
            };

            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (IOException _ignore) {
                }
        }

    }


}
