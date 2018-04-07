package PostService;

import com.arangodb.ArangoDB;
import com.rabbitmq.client.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

public class postService {

    private static final String RPC_QUEUE_NAME = "post";
    private ThreadPoolExecutor executor;
    protected static ArangoInstance arangoInstance;

    public postService(int threadsNo,int maxDbConnection){
         executor= (ThreadPoolExecutor) Executors.newFixedThreadPool(threadsNo);
         arangoInstance = new ArangoInstance("root","pass",maxDbConnection);
    }




    public void setMaxThreadsSize(int size){
        executor.setMaximumPoolSize(size);
    }

    public void setMaxDBConnections(int maxDBConnections){
        arangoInstance.setMaxDBConnections(maxDBConnections);
    }



    public void Start(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            final Channel channel = connection.createChannel();

            channel.queueDeclare(RPC_QUEUE_NAME, true, false, false, null);
//            channel.queueDeclare(RPC_RESPONSE_QUEUE, false, false, false, null);

            channel.basicQos(1);

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
                        JSONParser parser = new JSONParser();
                        JSONObject command = (JSONObject) parser.parse(message);
                        String className = (String)command.get("command");
                        System.out.println(className);
                        Class com = Class.forName("PostService."+className);
                        Command cmd = (Command) com.newInstance();

                        HashMap<String, Object> init = new HashMap<String, Object>();
                        init.put("channel", channel);
                        init.put("properties", properties);
                        init.put("replyProps", replyProps);
                        init.put("envelope", envelope);
                        init.put("body", message);

                        cmd.init(init,arangoInstance);
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


    public static void main(String [] argv) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
            postService postApp = new postService(15,15);
            postApp.Start();

    }


}
