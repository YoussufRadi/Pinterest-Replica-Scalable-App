package ClientService;

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
    int containerPort;

    public Client(String server, int port, int containerPort) {
        this.server = server;
        this.port = port;
        this.containerPort = containerPort;
    }

    public static void main(String[] args) {
        String server = "localhost";
        int port = 5252;
        int containerPort = 8094;
        new Client(server, port, containerPort).start();
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