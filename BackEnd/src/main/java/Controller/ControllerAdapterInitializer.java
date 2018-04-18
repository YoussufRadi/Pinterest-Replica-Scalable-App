package Controller;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class ControllerAdapterInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast("decoder", new ObjectDecoder(ClassResolvers
                .cacheDisabled(getClass().getClassLoader())));
        pipeline.addLast("encoder", new ObjectEncoder());
        pipeline.addLast("handler", new ControllerAdapterHandler());

    }

}