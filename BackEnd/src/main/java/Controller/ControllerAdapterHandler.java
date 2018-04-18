package Controller;

import Models.ControlCommand;
import Models.ControlMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ControllerAdapterHandler extends
        ChannelInboundHandlerAdapter {

    public static final ChannelGroup channels = new DefaultChannelGroup(
            "containers", GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[START] New Container has been initialized " + ctx.channel().localAddress());
        channels.add(ctx.channel());
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[END] A Container has been removed");
        channels.remove(ctx.channel());
        super.handlerRemoved(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext arg0, Object arg1) {
        Channel currentChannel = arg0.channel();

        if(arg1 instanceof ControlMessage){
            ControlMessage m = (ControlMessage) arg1;
            if(m.getControlCommand().equals(ControlCommand.initialize))
                Controller.Controller.services.get(m.getParam()).add(currentChannel);

            System.out.println("New Service connected : " + m.getParam() + ", id : " + (Controller.Controller.services.get(m.getParam()).size()-1));
        }

        System.out.println("[INFO] - " + currentChannel.remoteAddress() + " - " + arg1.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext arg0) {
        System.out.println("channelReadComplete");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext arg0) {
        System.out.println("channelWritabilityChanged");
        System.out.println();
    }

}