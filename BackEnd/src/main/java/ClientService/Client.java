package ClientService;

import Config.Config;
import Interface.ControlService;
import Models.ControlCommand;
import Models.ControlMessage;
import Models.ErrorLog;
import Models.ServicesType;
import Services.ChatService;
import Services.PostService;
import Services.UserService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;

public class Client {

    public static Channel channel;
    protected ControlService service;
    Config conf = Config.getInstance();
    private String server = conf.getControllerHost();
    private int port = conf.getControllerPort();
    private String serviceName;

//    public static void main(String[] args) {
//        Client c = new Client();
//        c.initService(ServicesType.post);
//        new Thread(() -> {
//            c.start();
//        }).start();
//        c.startService();
//    }

    public void initDB(){
        service.initDB();
    }

    public void initService(ServicesType serviceName) {
        switch (serviceName) {
            case user:
                service = new UserService();
                this.serviceName = conf.getMqInstanceUserQueue();
                break;
            case post:
                service = new PostService();
                this.serviceName = conf.getMqInstancePostQueue();
                break;
            case chat:
                service = new ChatService();
                this.serviceName = conf.getMqInstanceChatQueue();
                break;
            // TODO ADD SERVICE
        }
    }

    public void startService() {
        this.service.start();
    }

    public void start() {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap clientBootstrap = new Bootstrap();

            clientBootstrap.group(group);
            clientBootstrap.channel(NioSocketChannel.class);
            clientBootstrap.remoteAddress(new InetSocketAddress(server, port));
            clientBootstrap.handler(new ClientAdapterInitializer(service));

            ChannelFuture channelFuture = clientBootstrap.connect().sync();
            Client.channel = channelFuture.channel();

//            Thread t = new Thread(() -> {
//                Scanner sc = new Scanner(System.in);
//                while (true){
//                    String line = sc.nextLine();
//                    ErrorLog l = new ErrorLog(LogLevel.ERROR, line);
//                    Client.channel.writeAndFlush(l);
//                }
//            });
//            t.start();

            Client.channel.writeAndFlush(new ControlMessage(ControlCommand.initialize, serviceName));
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Client.channel.writeAndFlush(new ErrorLog(LogLevel.ERROR,errors.toString()));
        } finally {
            group.shutdownGracefully();
//            System.exit(0);
        }
    }
}