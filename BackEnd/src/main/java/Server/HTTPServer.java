package Server;

import Config.Config;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HTTPServer {

    private int port;

    private void start() {

        Config c = Config.getInstance();
        port = c.getWebServerPort();
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
//                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HTTPServerInitializer(port));
//            b.option(ChannelOption.SO_KEEPALIVE, true);
            Channel ch = b.bind(port).sync().channel();

            System.err.println("Controller is listening on http://127.0.0.1:" + port + '/');

            ch.closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {

        HTTPServer s = new HTTPServer();
        s.start();
    }
}
