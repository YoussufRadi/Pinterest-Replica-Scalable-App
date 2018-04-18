package ClientService;

import Models.ControlMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientAdapterHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext arg0, Object arg1) {
        System.out.println("Client Reading Channel");
        Channel currentChannel = arg0.channel();
        if(arg1 instanceof String) {
            System.out.println("[INFO] - " + currentChannel.remoteAddress() + " - " + arg1.toString());
        }
        else if(arg1 instanceof ControlMessage)
            controlService((ControlMessage) arg1);
        System.out.println();
        currentChannel.writeAndFlush("[Controller] - Success" + "\r\n");
    }

    private void controlService(ControlMessage m){
        switch (m.getControlCommand()){
            case maxDbConnections:  Client.service.setMaxDBConnections(Integer.parseInt(m.getParam()));
                break;
            case maxThreadPool : Client.service.setMaxThreadsSize(Integer.parseInt(m.getParam()));
                break;
            case resume : Client.service.resume();
                break;
            case freeze : Client.service.freeze();
                break;
            case addCommand : Client.service.add_command(m.getParam(), m.getPath());
                break;
            case deleteCommand : Client.service.delete_command(m.getParam());
                break;
            case updateCommand : Client.service.update_command(m.getParam(), m.getPath());
                break;
            case errorReportingLevel : Client.service.set_error_reporting_level(Integer.parseInt(m.getParam()));
                break;
        }
        System.out.println("ControlService is executing : " + m.getControlCommand());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext arg0) {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext arg0) {

    }

}