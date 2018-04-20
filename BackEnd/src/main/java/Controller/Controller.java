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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class Controller {

    Config conf = Config.getInstance();

    private int port = conf.getControllerPort();
    public static TreeMap<String,ArrayList<Channel>> services;
    public static final Logger logger = LogManager.getLogger(Controller.class);

    public static void main(String[] args) {
        new Controller().start();
    }

    public Controller(){

        InternalLoggerFactory.setDefaultFactory(Log4J2LoggerFactory.INSTANCE);

        services = new TreeMap<>();
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
            System.err.println("Controller is listening on http://127.0.0.1:" + port + '/');

            takeConsoleInput();

            bootstrap.bind(port).sync().channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
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

                if(line.length < 3){
                    System.out.println("Parameters are not enough");
                    continue;
                }

                ControlMessage controlMessage;
                ControlCommand c = getCommand(line[2]);
                if(c==null){
                    System.out.println("Command is not valid");
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
            case "error" : return ControlCommand.errorReportingLevel;
        }
        return null;
    }

    private void sendToChannel(String name, String id, ControlMessage m){
        if(services.containsKey(name)){
            ArrayList<Channel> channels = services.get(name);
            if(channels.size() > Integer.parseInt(id)){
                Controller.services.get(name).get(Integer.parseInt(id)).writeAndFlush(m);
                System.out.println("Send to Channel " + name + ", Command : " + m.getControlCommand());
            }
            else System.out.println("Service Id : " + id + " doesn't exist");
        }
        else System.out.println("Service Name : " + name + " doesn't exist");
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
            e.printStackTrace();
        }
        return sourceCode.toString();
    }
    // Example
    // post 0 add GetKhara /home/aboelenien/Desktop/GetKhara.txt
}
