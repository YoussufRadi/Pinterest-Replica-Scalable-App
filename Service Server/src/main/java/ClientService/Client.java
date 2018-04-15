package ClientService;

import Interface.ControlService;
//import Service.PostService;
//import Service.UserService;
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

    String server;
    int port;
    ControlService service;

    public Client(String server, int port, String serviceName) {
        this.server = server;
        this.port = port;
        switch (serviceName.toLowerCase()){
//            case "post": service = new PostService(); break;
//            case "user": service = new UserService(); break;
//            case "chat": service = new ChatService(); break;
        }
//        service.init(15,15);
//        service.start();
    }

    public static void main(String[] args) {
        String server = "localhost";
        int port = 5252;
        new Client(server, port,"post").start();
    }

    public static Channel channel;

    public void start() {
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

            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
            System.exit(0);
        }
    }
}