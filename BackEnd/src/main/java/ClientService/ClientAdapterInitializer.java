package ClientService;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


public class ClientAdapterInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        pipeline.addLast("decoder", new ObjectDecoder(ClassResolvers
                .cacheDisabled(getClass().getClassLoader())));
        pipeline.addLast("encode", new ObjectEncoder());
        pipeline.addLast("handler", new ClientAdapterHandler());
    }

}