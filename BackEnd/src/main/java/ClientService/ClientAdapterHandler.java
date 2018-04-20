package ClientService;

import Interface.ControlService;
import Models.ControlMessage;
import Models.ErrorLog;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.logging.LogLevel;

public class ClientAdapterHandler extends ChannelInboundHandlerAdapter {

    private final ControlService service;

    public ClientAdapterHandler(ControlService service) {
        this.service = service;
    }

    @Override
    public void channelRead(ChannelHandlerContext arg0, Object arg1) {
        Client.channel.writeAndFlush(new ErrorLog(LogLevel.DEBUG,"Client Reading Channel"));
        Channel currentChannel = arg0.channel();
        if(arg1 instanceof String) {
            Client.channel.writeAndFlush(new ErrorLog(LogLevel.INFO,"[INFO] - " + currentChannel.remoteAddress() + " - " + arg1.toString()));
        }
        else if(arg1 instanceof ControlMessage)
            controlService((ControlMessage) arg1);
        currentChannel.writeAndFlush("[Controller] - Success" + "\r\n");
    }

    private void controlService(ControlMessage m){
        switch (m.getControlCommand()){
            case maxDbConnections:  service.setMaxDBConnections(Integer.parseInt(m.getParam()));
                break;
            case maxThreadPool : service.setMaxThreadsSize(Integer.parseInt(m.getParam()));
                break;
            case resume : service.resume();
                break;
            case freeze : service.freeze();
                break;
            case addCommand : service.add_command(m.getParam(), m.getPath());
                break;
            case deleteCommand : service.delete_command(m.getParam());
                break;
            case updateCommand : service.update_command(m.getParam(), m.getPath());
                break;
        }
        Client.channel.writeAndFlush(new ErrorLog(LogLevel.DEBUG,"ControlService is executing : " + m.getControlCommand()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext arg0) {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext arg0) {

    }

}