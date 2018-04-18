package ClientService;

import Config.Config;
import Interface.ControlService;
import Models.ControlCommand;
import Models.ControlMessage;
import Models.ServicesType;
import Services.PostService;
import Services.UserService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class Client {

    Config conf = Config.getInstance();

    private String server = conf.getControllerHost();
    private int port = conf.getControllerPort();

    private String serviceName;
    protected static ControlService service;
    private static Channel channel;

    public void initService(ServicesType serviceName){
        switch (serviceName){
            case user:
            service = new UserService();
            this.serviceName = conf.getMqInstanceUserQueue();
            break;
            case post:
                service = new PostService();
                this.serviceName = conf.getMqInstancePostQueue();
                break;
            //TODO Chat Service
        }
    }


    public void startService() {
        Client.service.start();
    }

//    public static void main(String[] args) {
//        Client c = new Client();
//        c.initService(ServicesType.post);
//        c.startService();
//        c.start();
//    }

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

//            Thread t = new Thread(() -> {
//                Scanner sc = new Scanner(System.in);
//                while (true){
//                    String line = sc.nextLine();
//                    Client.channel.writeAndFlush(line + "\r\n");
//                }
//            });
//            t.start();

            Client.channel.writeAndFlush(new ControlMessage(ControlCommand.initialize, serviceName));

            channelFuture.channel().closeFuture().sync();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
            System.exit(0);
        }
    }
}