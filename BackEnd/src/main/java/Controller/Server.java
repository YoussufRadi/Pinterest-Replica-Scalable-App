package Controller;

import Models.ControlMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class Server {

    private int port;
    public static TreeMap<String,ArrayList<Channel>> services;

    public static void main(String[] args) {
        new Server(5252).start();
    }

    private Server(int port){
        this.port = port;
        services = new TreeMap<>();
        //TODO Chat Service
        services.put("user", new ArrayList<>());
        services.put("post", new ArrayList<>());
    }

    private void start() {
        EventLoopGroup producer = new NioEventLoopGroup(2);
        EventLoopGroup consumer = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(producer, consumer)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerAdapterInitializer());
            System.out.println("Server started");
            System.out.println();

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

        ControlMessage dbThreads = new ControlMessage("db","10");
        ControlMessage exThreads = new ControlMessage("thread", "10");
        ControlMessage resume = new ControlMessage("resume");
        ControlMessage add = new ControlMessage("add", "newCommand", "/Command/path");
        ControlMessage delete = new ControlMessage("delete", "newCommand");
        ControlMessage update = new ControlMessage("update", "newCommand", "/Command/path");
        ControlMessage error = new ControlMessage("error", "1");

        Thread t = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true){
                String line[] = sc.nextLine().split(" ");

                ControlMessage freeze = new ControlMessage("freeze");
//                for(Channel c : ServerAdapterHandler.channels)
//                    c.writeAndFlush("hello");
                if(services.containsKey(line[0])){
                    ArrayList<Channel> channels = services.get(line[0]);
                    if(channels.size() > Integer.parseInt(line[1])){
                        Server.services.get(line[0]).get(Integer.parseInt(line[1])).writeAndFlush(freeze).notify();
                    }
                    else System.out.println("Service Id : " + line[1] + " doesn't exist");
                }
                else System.out.println("Service Name : " + line[0] + " doesn't exist");
            }
        });
        t.start();
    }

}
