package Controller;

import Models.ControlMessage;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.TreeMap;

public class ControllerCommand {

    private String serviceName;
    private int serviceId;

    private ControllerCommand(String serviceName, int serviceId) {
        this.serviceName = serviceName;
        this.serviceId = serviceId;
    }
    public void executeControl(ControlMessage message){
        TreeMap<String, ArrayList<Channel>> services = Server.services;
        if(services.containsKey(serviceName)){
            ArrayList<Channel> channels = services.get(serviceName);
            if(channels.size() < serviceId){
                Server.services.get(serviceName).get(serviceId).writeAndFlush(message);
            }
            else System.out.println("Service Id : " + serviceId + " doesn't exist");
        }
        else System.out.println("Service Name : " + serviceName + " doesn't exist");
    }


    public static void main(String[] args) {


//        ControllerCommand controller = new ControllerCommand("post", 0);
//        controller.executeControl(freeze);
    }
}
