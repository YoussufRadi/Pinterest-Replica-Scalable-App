package Netty;

import com.rabbitmq.client.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeoutException;

import static io.netty.buffer.Unpooled.copiedBuffer;

public class HTTPServerInitializer extends ChannelInitializer<SocketChannel> {

    HashMap<String, ChannelHandlerContext> uuid = new HashMap<String, ChannelHandlerContext>();
    ConnectionFactory factory;
    Channel channel;
    private String RPC_QUEUE;

    @Override
    protected void initChannel(SocketChannel arg0) {
        RPC_QUEUE = arg0.remoteAddress() + "" + arg0.localAddress();

        establishConnection();
        serverQueue();

        CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin()
                .allowedRequestHeaders("X-Requested-With", "Content-Type", "Content-Length")
                .allowedRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS)
                .build();
        ChannelPipeline p = arg0.pipeline();
        p.addLast("decoder", new HttpRequestDecoder());
        p.addLast("encoder", new HttpResponseEncoder());
        p.addLast(new CorsHandler(corsConfig));
        p.addLast(new HTTPHandler());
        p.addLast("MQ", new RequestHandler(uuid, RPC_QUEUE));
//        p.addLast("aggregator",
//                new HttpObjectAggregator(512 * 1024));
//        p.addLast("request",new CustumHandler());

    }

    private void establishConnection() {
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

    private void serverQueue() {
        try {
            channel.queueDeclare(RPC_QUEUE, false, false, false, null);
            channel.basicQos(1);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                            .Builder()
                            .correlationId(properties.getCorrelationId())
                            .build();
                    System.out.println("Responding to corrID: " + properties.getCorrelationId());
                    String responseMsg = new String(body, "UTF-8");


                    JSONObject responseJson = new JSONObject(responseMsg);


                    FullHttpResponse response = new DefaultFullHttpResponse(
                            HttpVersion.HTTP_1_1,
                            HttpResponseStatus.OK,
                            copiedBuffer(responseJson.get("response").toString().getBytes()));

                    JSONObject headers = (JSONObject) responseJson.get("Headers");
                    Iterator<String> keys = headers.keys();

                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        String value = (String) headers.get(key);
                        response.headers().set(key, value);
                    }

                    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
                    response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

                    System.out.println("Response   :   " + responseJson.get("response"));
                    System.out.println();

                    ChannelHandlerContext ctxRec = uuid.remove(properties.getCorrelationId());
                    ctxRec.writeAndFlush(response);
                    ctxRec.close();
                }
            };
            channel.basicConsume(RPC_QUEUE, true, consumer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

