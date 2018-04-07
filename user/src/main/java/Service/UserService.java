package Service;


import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

import Cache.UserCacheController;
import com.rabbitmq.client.*;
import Commands.Command;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UserService {

    private static final String RPC_QUEUE_NAME = "user";

    private static ThreadPoolExecutor executor;
    private final int threadsNo;
    private Cache.UserCacheController UserCacheController;

    private UserService(int threadsNo){
        this.threadsNo = threadsNo;

        executor= (ThreadPoolExecutor) Executors.newFixedThreadPool(threadsNo);
        try {
            UserCacheController = new UserCacheController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setMaxThreadsSize(int size){
        executor.setMaximumPoolSize(size);
    }


    private void start() {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");


        Connection connection = null;
        try {
            connection      = factory.newConnection();
            final Channel channel = connection.createChannel();
            channel.queueDeclare(RPC_QUEUE_NAME, true, false, false, null);
            channel.basicQos(threadsNo);
            System.out.println(" [x] Awaiting RPC requests");
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(properties.getCorrelationId())
                            .build();

                    String response = "";

                    try {
                        String message = new String(body, "UTF-8");
                        JSONParser parser = new JSONParser();
                        JSONObject command = (JSONObject) parser.parse(message);
                        String className = "Commands." + (String)command.get("command");
                        Class com = Class.forName(className);
                        Command cmd = (Command) com.newInstance();

                        HashMap<String, Object> init2 = new HashMap<String, Object>();
                        init2.put("channel", channel);
                        init2.put("properties", properties);
                        init2.put("replyProps", replyProps);
                        init2.put("envelope", envelope);
                        init2.put("body", message);
                        init2.put("UserCacheController", UserCacheController);
                        cmd.init(init2);
                        executor.submit(cmd);
                    }
                    catch (RuntimeException e){
                        System.out.println(" [.] " + e.toString());
                    }  catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();

                    }
                }
            };
            channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
            // Wait and be prepared to consume the message from RPC client.
            while (true) {
                synchronized(consumer) {
                    try {
                        consumer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                try {
                    connection.close();
                } catch (IOException _ignore) {}
        }
    }

    public static void main(String[] argv) throws IOException {
        UserService rpcServer = new UserService(15);
        rpcServer.start();

    }

}



