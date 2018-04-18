package MediaServer;

import Config.Config;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public final class MediaServer {

    Config conf = Config.getInstance();

    private int PORT = conf.getMediaServerPort();
    private int THREADS = conf.getMediaServerThreads();

    private MediaServer(){
        EventLoopGroup bossGroup = new NioEventLoopGroup(THREADS);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new MediaFileServerInitializer());

            Channel ch = b.bind(PORT).sync().channel();

            System.err.println("Open your web browser and navigate to http" +"://127.0.0.1:" + PORT + '/');

            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new MediaServer();

    }
}