package ClientService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientAdapterHandler extends ChannelInboundHandlerAdapter {

//
//    public void messageReceived(ChannelHandlerContext context, String message)
//            throws Exception {
//        System.out.println(message);
//        if (message.equals("quit"))
//            throw new Exception("Server is closed");
//    }

    @Override
    public void channelRead(ChannelHandlerContext arg0, Object arg1) {
        // TODO Auto-generated method stub
        Channel currentChannel = arg0.channel();
        System.out.println("[INFO] - " + currentChannel.remoteAddress() + " - " + arg1.toString());
        currentChannel.writeAndFlush("[Server] - Success" + "\r\n");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext arg0) {
        // TODO Auto-generated method stub
//        arg0.flush();
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext arg0) {
        // TODO Auto-generated method stub

    }

}