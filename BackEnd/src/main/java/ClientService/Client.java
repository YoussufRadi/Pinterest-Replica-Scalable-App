package ClientService;

import Interface.ControlService;
import Models.ControlMessage;
import Services.PostService;
import Services.UserService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class Client {

    private String server;
    private String serviceName;
    private int port;
    protected static ControlService service;
    private static Channel channel;

    private Client(String server, int port) {
        this.server = server;
        this.port = port;
    }

    private void initService(String serviceName, String host, int port, int threadNo, int dbConnections){
        this.serviceName = serviceName;
        switch (this.serviceName.toLowerCase()){
            case "post": service = new PostService(host,port,threadNo,dbConnections); break;
            case "user": service = new UserService(host,port,threadNo,dbConnections); break;
        }
    }

    public static void main(String[] args) {
        String server = "localhost";
        int port = 5252;
        Client c = new Client(server, port);
        c.initService("post","localhost",5672,15,15);
        c.start();
    }

    private void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap clientBootstrap = new Bootstrap();

            clientBootstrap.group(group);
            clientBootstrap.channel(NioSocketChannel.class);
            clientBootstrap.remoteAddress(new InetSocketAddress(server, port));
            clientBootstrap.handler(new ClientAdapterInitializer());

            ChannelFuture channelFuture = clientBootstrap.connect().sync();
            channel = channelFuture.channel();


            Thread t = new Thread(() -> {
                Scanner sc = new Scanner(System.in);
                while (true){
                    String line = sc.nextLine();
                    Client.channel.writeAndFlush(line + "\r\n");
                }
            });
            t.start();

            Client.channel.writeAndFlush(new ControlMessage("init", serviceName));

            channelFuture.channel().closeFuture().sync();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
            System.exit(0);
        }
    }
}