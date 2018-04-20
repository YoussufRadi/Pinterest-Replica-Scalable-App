package Interface;

import Cache.UserCacheController;
import ClientService.Client;
import Config.Config;
import Config.ConfigTypes;
import Database.ArangoInstance;
import Models.ErrorLog;
import com.rabbitmq.client.*;
import io.netty.handler.logging.LogLevel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.redisson.api.RLiveObjectService;

import java.io.*;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

public abstract class ControlService {

    protected Config conf = Config.getInstance();

    private int threadsNo = conf.getServiceMaxThreads();
    protected int maxDBConnections = conf.getServiceMaxDbConnections();
    private  ThreadPoolExecutor executor;

    protected String RPC_QUEUE_NAME; //set by init
    private String host = conf.getMqInstanceQueueHost();
    private int port = conf.getMqInstanceQueuePort();
    private String user = conf.getMqInstanceQueueUserName();
    private String pass = conf.getMqInstanceQueuePass();
    private Channel channel;
    private String consumerTag;
    private Consumer consumer;

    protected RLiveObjectService liveObjectService; // For Post Only
    protected ArangoInstance arangoInstance; // For Post Only
    protected UserCacheController userCacheController; // For UserModel Only

    public ControlService(){
        this.executor= (ThreadPoolExecutor) Executors.newFixedThreadPool(threadsNo);
        init();
    }

    public abstract void init();

    public void start(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(user);
        factory.setPassword(pass);
        Connection connection = null;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(RPC_QUEUE_NAME, true, false, false, null);
            channel.basicQos(threadsNo);

//            Client.channel.writeAndFlush(new ErrorLog(LogLevel.INFO," [x] Awaiting RPC requests on Queue : " + RPC_QUEUE_NAME));
            consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(properties.getCorrelationId())
                            .build();
                    Client.channel.writeAndFlush(new ErrorLog(LogLevel.INFO,"Responding to corrID: " + properties.getCorrelationId() +  ", on Queue : " + RPC_QUEUE_NAME));


                    try {
                        //Using Reflection to convert a command String to its appropriate class
                        String message = new String(body, "UTF-8");
                        JSONParser parser = new JSONParser();
                        JSONObject command = (JSONObject) parser.parse(message);
                        String className = (String) command.get("command");
                        Class com = Class.forName(RPC_QUEUE_NAME+ "Commands." + className);
                        Command cmd = (Command) com.newInstance();

                        TreeMap<String, Object> init = new TreeMap<>();
                        init.put("channel", channel);
                        init.put("properties", properties);
                        init.put("replyProps", replyProps);
                        init.put("envelope", envelope);
                        init.put("body", message);
                        init.put("RLiveObjectService", liveObjectService);
                        init.put("ArangoInstance", arangoInstance);
                        init.put("UserCacheController", userCacheController);
                        cmd.init(init);
                        executor.submit(cmd);
                    } catch (RuntimeException | ParseException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                        StringWriter errors = new StringWriter();
                        e.printStackTrace(new PrintWriter(errors));
                        Client.channel.writeAndFlush(new ErrorLog(LogLevel.ERROR, errors.toString()));                        start();
                    } finally {
                        synchronized (this) {
                            this.notify();
                        }
                    }
                }
            };
            consumerTag = channel.basicConsume(RPC_QUEUE_NAME, true, consumer);


        } catch (IOException | TimeoutException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Client.channel.writeAndFlush(new ErrorLog(LogLevel.ERROR, errors.toString()));
            start();
        }
    }

    public void setMaxDBConnections(int connections){
        setDBConnections(connections);
        conf.setProperty(ConfigTypes.Service, "service.max.db", String.valueOf(connections));

    }

    protected abstract void setDBConnections(int connections);

    public void setMaxThreadsSize(int threads){
        threadsNo =threads;
        executor.setMaximumPoolSize(threads);
        conf.setProperty(ConfigTypes.Service, "service.max.thread", String.valueOf(threads));
    }

    public void resume() {
        try {
            consumerTag = channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
        } catch (IOException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Client.channel.writeAndFlush(new ErrorLog(LogLevel.ERROR, errors.toString()));        }
        Client.channel.writeAndFlush(new ErrorLog(LogLevel.INFO,"Service Resumed"));
    }

    public void freeze() {
        try {
            channel.basicCancel(consumerTag);
        } catch (IOException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Client.channel.writeAndFlush(new ErrorLog(LogLevel.ERROR, errors.toString()));        }
        Client.channel.writeAndFlush(new ErrorLog(LogLevel.INFO,"Service Freezed"));
    }

    //TODO CHECK IF FILE EXISTS FIRST IF THERE THEN LOG AN ERROR
    public void add_command(String commandName, String source_code){
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter("/target/classes/"+RPC_QUEUE_NAME+"Commands/"+commandName+".class");
            BufferedWriter bufferedWriter =
                    new BufferedWriter(fileWriter);
            bufferedWriter.write(source_code);
            bufferedWriter.close();
        } catch (IOException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Client.channel.writeAndFlush(new ErrorLog(LogLevel.ERROR, errors.toString()));        }

    }

    //TODO
    public void delete_command(String commandName){

    }

    public void update_command(String commandName, String filePath){
        delete_command(commandName);
        add_command(commandName, filePath);
    }
}
