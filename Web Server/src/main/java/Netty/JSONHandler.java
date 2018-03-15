package Netty;

import RabbitMQ.RPCClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.json.JSONException;
import org.json.JSONObject;

import static io.netty.buffer.Unpooled.copiedBuffer;

public class JSONHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("JSON HANDLER");
        ByteBuf buffer = (ByteBuf) o;
        //try and catch
        try{
            JSONObject jsonObject = new JSONObject(buffer.toString(CharsetUtil.UTF_8));
            System.out.println(jsonObject.toString());
        } catch (JSONException e) {
            System.out.println("No JSON");
            System.out.println("String");
            System.out.println((String) buffer.toString(CharsetUtil.UTF_8));
        }


//        RPCClient rpcClient = new RPCClient();
//        String response = rpcClient.call(jsonObject.toString());

        String responseMessage = "response";
        FullHttpResponse response2 = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                copiedBuffer(responseMessage.getBytes()));
        response2.headers().set(HttpHeaders.Names.CONTENT_TYPE,
                "text/plain");
        response2.headers().set(HttpHeaders.Names.CONTENT_LENGTH,
                responseMessage.length());
        channelHandlerContext.writeAndFlush(response2);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.writeAndFlush(new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.INTERNAL_SERVER_ERROR,
                copiedBuffer(cause.getMessage().getBytes())
        ));
    }

}
