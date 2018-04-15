package ClientService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientAdapterHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext arg0, Object arg1) {
        Channel currentChannel = arg0.channel();
        System.out.println("[INFO] - " + currentChannel.remoteAddress() + " - " + arg1.toString());
        currentChannel.writeAndFlush("[Server] - Success" + "\r\n");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext arg0) {

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext arg0) {

    }

}