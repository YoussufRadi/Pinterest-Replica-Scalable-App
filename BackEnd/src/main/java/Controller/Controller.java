package Controller;

import Config.Config;
import Models.ControlCommand;
import Models.ControlMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Log4J2LoggerFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class Controller {

    Config conf = Config.getInstance();

    private int port = conf.getControllerPort();
    public static TreeMap<String,ArrayList<Channel>> services= new TreeMap<>();
    public static final Logger logger = LogManager.getLogger(Controller.class);

    public static void main(String[] args) {
        new Controller().start();
    }

    public Controller(){

        InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);

        services.put(conf.getMqInstanceUserQueue(), new ArrayList<>());
        services.put(conf.getMqInstancePostQueue(), new ArrayList<>());
        services.put(conf.getMqInstanceChatQueue(), new ArrayList<>());
    }

    public void start() {

        EventLoopGroup producer = new NioEventLoopGroup();
        EventLoopGroup consumer = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(producer, consumer)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ControllerAdapterInitializer());
//                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            logger.info("Controller is listening on http://127.0.0.1:" + port + '/');

            takeConsoleInput();

            bootstrap.bind(port).sync().channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            logger.error(errors.toString());
        } finally {
            producer.shutdownGracefully();
            consumer.shutdownGracefully();
        }
    }

    private void takeConsoleInput(){
        Thread t = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true){
                String line[] = sc.nextLine().split(" ");

                if(line.length < 2){
                    logger.error("Parameters are not enough");
                    continue;
                }
                if(line[0].equals("error")){
                    setLoggerLevel(line[1]);
                    continue;
                }

                if(line.length < 3){
                    logger.error("Control commands must be of length 3 at least");
                    continue;
                }

                ControlMessage controlMessage;
                ControlCommand c = getCommand(line[2]);
                if(c==null){
                    logger.error("Command is not valid");
                    continue;
                }
                if(line.length == 3)
                    controlMessage =  new ControlMessage(c);
                else if(line.length == 4)
                    controlMessage =  new ControlMessage(c, line[3]);
                else
                    controlMessage =  new ControlMessage(c,line[3],readFile(line[4]));

                sendToChannel(line[0],line[1],controlMessage);
            }
        });
        t.start();
    }

    private ControlCommand getCommand(String c){
        switch (c.toLowerCase()){
            case "db" : return ControlCommand.maxDbConnections;
            case "thread" : return ControlCommand.maxThreadPool;
            case "freeze" : return ControlCommand.freeze;
            case "resume" : return ControlCommand.resume;
            case "add" : return ControlCommand.addCommand;
            case "delete" : return ControlCommand.deleteCommand;
            case "update" : return ControlCommand.updateCommand;
            case "seedpostdb" : return ControlCommand.seedPostDB;
            case "createpostdb" : return ControlCommand.createPostDB;
            case "droppostdb" : return ControlCommand.dropPostDB;
        }
        return null;
    }

    private void sendToChannel(String name, String id, ControlMessage m){
        if(services.containsKey(name)){
            ArrayList<Channel> channels = services.get(name);
            if(channels.size() > Integer.parseInt(id)){
                Controller.services.get(name).get(Integer.parseInt(id)).writeAndFlush(m);
                logger.info("Send to Channel " + name + ", Command : " + m.getControlCommand());
            }
            else logger.error("Service Id : " + id + " doesn't exist");
        }
        else logger.error("Service Name : " + name + " doesn't exist");
    }

    private String readFile(String path){
        StringBuilder sourceCode  = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            String nline;

            while((nline = bufferedReader.readLine()) != null) {
                sourceCode.append(nline).append("\n");
            }

        } catch (IOException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            logger.error(errors.toString());
            e.printStackTrace();
        }
        return sourceCode.toString();
    }

    private void setLoggerLevel(String loggerLevel) {
        switch (loggerLevel.toLowerCase()){
            case "trace":
                Configurator.setRootLevel(Level.TRACE);
                break;
            case "debug":
                Configurator.setRootLevel(Level.DEBUG);
                break;
            case "info":
                Configurator.setRootLevel(Level.INFO);
                break;
            case "warn":
                Configurator.setRootLevel(Level.WARN);
                break;
            case "error":
                Configurator.setRootLevel(Level.ERROR);
                break;
            case "off":
                Configurator.setRootLevel(Level.OFF);
                break;
        }
    }
    // Example
    // post 0 add GetKhara /home/aboelenien/Desktop/GetKhara.txt
}
