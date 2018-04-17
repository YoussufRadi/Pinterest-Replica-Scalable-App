package Controller;

import Models.ControlMessage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
                    .childHandler(new ServerAdapterInitializer())
                    .childOption(ChannelOption.SO_KEEPALIVE, true);;
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
        ControlMessage freeze = new ControlMessage("freeze");

        Thread t = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true){
                String line[] = sc.nextLine().split(" ");

                if(line.length < 3){
                    System.out.println("Parameters are not enough");
                    continue;
                }

                ControlMessage controlMessage;
                if(line.length == 3)
                    controlMessage =  new ControlMessage(line[2]);
                else if(line.length == 4)
                    controlMessage =  new ControlMessage(line[2], line[3]);
                else
                    controlMessage =  new ControlMessage(line[2],line[3],readFile(line[4]));

                sendToChannel(line[0],line[1],controlMessage);
            }
        });
        t.start();
    }

    private void sendToChannel(String name, String id, ControlMessage m){
        if(services.containsKey(name)){
            ArrayList<Channel> channels = services.get(name);
            if(channels.size() > Integer.parseInt(id)){
                Server.services.get(name).get(Integer.parseInt(id)).writeAndFlush(m);

            }
            else System.out.println("Service Id : " + id + " doesn't exist");
        }
        else System.out.println("Service Name : " + name + " doesn't exist");
    }

    private String readFile(String path){
        String sourceCode  = "";
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            String nline = null;

            while((nline = bufferedReader.readLine()) != null) {
                sourceCode+=nline+"\n";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return sourceCode;
    }
    // Example
    // post 0 add GetKhara /home/aboelenien/Desktop/GetKhara.txt
}
