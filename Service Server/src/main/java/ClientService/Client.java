package ClientService;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

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

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap().group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ClientAdapterInitializer());

            Channel channel = bootstrap.connect(server, port).sync().channel();

            channel.write("Hi\n");
            channel.write("Hi\n");
            channel.write("Hi\n");
            channel.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}