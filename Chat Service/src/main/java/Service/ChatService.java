package Service;

import Commands.Command;
import Database.ArangoInstance;
import com.rabbitmq.client.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.redisson.api.RLiveObjectService;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

public class ChatService extends ControlService {
    protected static ArangoInstance arangoInstance;

    private static final String RPC_QUEUE_NAME = "chat";
    public void setMaxDBConnections(int connections){
        this.maxDBConnections = connections;
    }


    @Override
    public void init(int thread, int connections) {

        executor= (ThreadPoolExecutor) Executors.newFixedThreadPool(thread);
        arangoInstance = new ArangoInstance("root","sinsin1234",connections);

    }

    public void start(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            channel.queueDeclare(RPC_QUEUE_NAME, true, false, false, null);
//            channel.queueDeclare(RPC_RESPONSE_QUEUE, false, false, false, null);

            channel.basicQos(threadsNo);

            System.out.println(" [x] Awaiting RPC requests");

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(properties.getCorrelationId())
                            .build();
                    System.out.println("Responding to corrID: "+ properties.getCorrelationId());

                    String response = "";

                    try {

                        //Using Reflection to convert a command String to its appropriate class
                        String message = new String(body, "UTF-8");
                      //  System.out.println(message);
                        JSONParser parser = new JSONParser();
                        JSONObject command = (JSONObject) parser.parse(message);
                        System.out.println(command);
                        String className = (String)command.get("command");
                        //System.out.println(className);
                        Class com = Class.forName("Commands."+className);
                        Command cmd = (Command) com.newInstance();

                        HashMap<String, Object> init = new HashMap<String, Object>();
                        init.put("channel", channel);
                        init.put("properties", properties);
                        init.put("replyProps", replyProps);
                        init.put("envelope", envelope);
                        init.put("body", message);
                        init.put("ArangoInstance", arangoInstance);
                        cmd.init(init);
                        executor.submit(cmd);

                    } catch (RuntimeException e) {
                        System.out.println(" [.] " + e.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
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
        }
    }


    public static void main(String [] args) {
        ControlService chatApp = new ChatService();
        chatApp.init(15,15);
        chatApp.start();
//        postApp.setMaxDBConnections(15);
//        postApp.setMaxThreadsSize(15);

    }


}
