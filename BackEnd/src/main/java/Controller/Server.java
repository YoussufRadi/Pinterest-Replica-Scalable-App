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
    protected static TreeMap<String,ArrayList<Channel>> services;

    public static void main(String[] args) {
        new Server(5252).start();
    }

    private Server(int port){
        this.port = port;
        services = new TreeMap<>();
        services.put("user", new ArrayList<>());
        services.put("post", new ArrayList<>());
    }

    private void start() {
        EventLoopGroup producer = new NioEventLoopGroup();
        EventLoopGroup consumer = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(producer, consumer)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerAdapterInitializer());
            System.out.println("Server started");
            System.out.println();

            Thread t = new Thread(() -> {
                Scanner sc = new Scanner(System.in);
                while (true){
                    String[] line = sc.nextLine().split(" ");
                    ControlMessage msg;
//                    if(line.length == 4)
//                        msg = new ControlMessage(line[0],line[1],line[2],line[3]);
//                    else
//                        msg = new ControlMessage(line[0],line[1],line[2],line[3],line[4]);
//                    ServerAdapterHandler.channels.
                    for(Channel c : ServerAdapterHandler.channels)
                        c.writeAndFlush(new ControlMessage("1","1","1"));
                }
            });
            t.start();
            bootstrap.bind(port).sync().channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.shutdownGracefully();
            consumer.shutdownGracefully();
        }

    }

}
