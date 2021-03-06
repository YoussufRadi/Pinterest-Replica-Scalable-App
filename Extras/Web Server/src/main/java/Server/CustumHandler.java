package Server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import static io.netty.buffer.Unpooled.copiedBuffer;

public class CustumHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                            throws Exception {
        if (msg instanceof FullHttpRequest) {
            final FullHttpRequest request = (FullHttpRequest) msg;
//            System.out.println(request.content().readableBytes());
            final String responseMessage = "Hello from Server!";

            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    copiedBuffer(responseMessage.getBytes())
            );

            if (HttpHeaders.isKeepAlive(request)) {
                response.headers().set(
                        HttpHeaders.Names.CONNECTION,
                        HttpHeaders.Values.KEEP_ALIVE
                );
            }
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE,
                    "text/plain");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,
                    responseMessage.length());

            ctx.writeAndFlush(response);
        } else {
            super.channelRead(ctx, msg);
        }
    }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx)
                            throws Exception {
        ctx.flush();
    }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx,
            Throwable cause) throws Exception {
        ctx.writeAndFlush(new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.INTERNAL_SERVER_ERROR,
                copiedBuffer(cause.getMessage().getBytes())
        ));
    }
}
