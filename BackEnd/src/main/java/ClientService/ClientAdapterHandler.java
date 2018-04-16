package ClientService;

import Models.ControlMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientAdapterHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext arg0, Object arg1) {
        Channel currentChannel = arg0.channel();
        if(arg1 instanceof String)
            System.out.println("[INFO] - " + currentChannel.remoteAddress() + " - " + arg1.toString());
        else if(arg1 instanceof ControlMessage){
            ControlMessage m = (ControlMessage) arg1;
            controlService(m);
            System.out.println("[INFO] - " + currentChannel.remoteAddress() + " - " + m.getControlCommand());
        }
        currentChannel.writeAndFlush("[Server] - Success" + "\r\n");
    }

    private void controlService(ControlMessage m){
        switch (m.getControlCommand()){
            case "db" : Client.service.setMaxDBConnections(Integer.parseInt(m.getParam()));
                break;
            case "thread" : Client.service.setMaxThreadsSize(Integer.parseInt(m.getParam()));
                break;
            case "resume" : Client.service.resume();
                break;
            case "freeze" : Client.service.freeze();
                break;
            case "add" : Client.service.add_command(m.getParam(), m.getPath());
                break;
            case "delete" : Client.service.delete_command(m.getParam());
                break;
            case "update" : Client.service.update_command(m.getParam(), m.getPath());
                break;
            case "error" : Client.service.set_error_reporting_level(Integer.parseInt(m.getParam()));
                break;
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext arg0) {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext arg0) {

    }

}