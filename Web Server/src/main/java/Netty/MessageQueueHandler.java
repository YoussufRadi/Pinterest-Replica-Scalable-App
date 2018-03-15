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

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static io.netty.buffer.Unpooled.copiedBuffer;

public class MessageQueueHandler extends ChannelInboundHandlerAdapter {


    ConnectionFactory factory;
    Channel channel;

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

            establishChannel(service,channelHandlerContext,corrId,jsonRequest);

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

    private void establishConnection(String RPC_QUEUE_NAME) {
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void transmitRequest(String RPC_QUEUE_NAME, String corrId, JSONObject jsonRequest){
        try {
            channel.queueDeclare(RPC_QUEUE_NAME + "-request", false, false, false, null);
            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(corrId)
                    .replyTo(RPC_QUEUE_NAME + "-response")
                    .build();
            System.out.println("Sent: "+ jsonRequest.toString());
            System.out.println();
            channel.basicPublish("", RPC_QUEUE_NAME + "-request", props, jsonRequest.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void establishChannel(String RPC_QUEUE_NAME, ChannelHandlerContext ctx, String corrId, JSONObject jsonRequest) {
        establishConnection(RPC_QUEUE_NAME);
        Channel currentChannel = channel;

        transmitRequest(RPC_QUEUE_NAME,corrId,jsonRequest);
        try {
            channel.queueDeclare(RPC_QUEUE_NAME + "-response", false, false, false, null);
            channel.basicQos(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Consumer consumer = new DefaultConsumer(currentChannel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(properties.getCorrelationId())
                        .build();
                System.out.println("Responding to corrID: " + properties.getCorrelationId());
                String responseMsg = new String(body, "UTF-8");
                System.out.println("Response   :   " + responseMsg);
                System.out.println();
                if (replyProps.getCorrelationId().equals(corrId)) {

                    JSONObject responseJson = new JSONObject(responseMsg);


                    FullHttpResponse response = new DefaultFullHttpResponse(
                            HttpVersion.HTTP_1_1,
                            HttpResponseStatus.OK,
                            copiedBuffer(responseJson.toString().getBytes()));

                    JSONObject headers = (JSONObject) jsonRequest.get("Headers");
                    Iterator<String> keys = headers.keys();

                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        String value = (String) headers.get(key);
                        response.headers().set(key, value);
                    }

                    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
                    response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

                    ctx.writeAndFlush(response);
                    ctx.close();
                    try {
//                        currentChannel.basicAck(envelope.getDeliveryTag(), false);
                        currentChannel.getConnection().close();
                        currentChannel.close();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }

            }

        };
        try {
            currentChannel.basicConsume(RPC_QUEUE_NAME + "-response", true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
//        ctx.close();
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
