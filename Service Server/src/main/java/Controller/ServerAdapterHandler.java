package Controller;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerAdapterHandler extends
        ChannelInboundHandlerAdapter {

    public static final ChannelGroup channels = new DefaultChannelGroup(
            "containers", GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[START] New Container has been initialzed");
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
        System.out.println("channelRead");
        Channel currentChannel = arg0.channel();
        System.out.println("[INFO] - " + currentChannel.remoteAddress() + " - " + arg1.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext arg0) {
        System.out.println("channelReadComplete");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext arg0) {
        System.out.println("channelWritabilityChanged");
    }

}