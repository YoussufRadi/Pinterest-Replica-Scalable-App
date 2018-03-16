package Netty;

import com.rabbitmq.client.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

import static io.netty.buffer.Unpooled.copiedBuffer;

public class RequestHandler extends ChannelInboundHandlerAdapter {


    ConnectionFactory factory = new ConnectionFactory();
    HashMap<String, ChannelHandlerContext> uuid;
    String RPC_QUEUE;

    public RequestHandler(HashMap<String, ChannelHandlerContext> uuid, String RPC_QUEUE) {
        this.uuid = uuid;
        this.RPC_QUEUE = RPC_QUEUE;
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        ByteBuf buffer = (ByteBuf) o;

        //try and catch
        try {
            JSONObject body = new JSONObject(buffer.toString(CharsetUtil.UTF_8));

            final JSONObject jsonRequest = (JSONObject) channelHandlerContext.channel().attr(AttributeKey.valueOf("REQUEST")).get();
            final String corrId = (String) channelHandlerContext.channel().attr(AttributeKey.valueOf("CORRID")).get();
            jsonRequest.put("body", body);
            jsonRequest.put("command", body.get("command"));
            String service = (String) body.get("application");
            jsonRequest.put("application", service);

            transmitRequest(service,corrId,jsonRequest,channelHandlerContext);

        } catch (JSONException e) {
            e.printStackTrace();
            String responseMessage = "NO JSON PROVIDED";
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.BAD_REQUEST,
                    copiedBuffer(responseMessage.getBytes()));
            channelHandlerContext.writeAndFlush(response);
        }

    }

    private void transmitRequest(String RPC_QUEUE_NAME, String corrId, JSONObject jsonRequest, ChannelHandlerContext ctx){
        try {
            uuid.put(corrId,ctx);
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare("load_balancer", false, false, false, null);
            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(corrId)
                    .replyTo(RPC_QUEUE)
                    .build();
            System.out.println("Sent: "+ jsonRequest.toString());
            System.out.println();
            channel.basicPublish("", "load_balancer", props, jsonRequest.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
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
