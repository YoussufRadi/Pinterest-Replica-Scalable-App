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
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static io.netty.buffer.Unpooled.copiedBuffer;

public class HTTPServerInitializer extends ChannelInitializer<SocketChannel> {

    private HashMap<String, ChannelHandlerContext> uuid = new HashMap<String, ChannelHandlerContext>();
    private ConnectionFactory factory;
    private Channel receiverChannel;
    private Channel senderChannel;
    private String FACTORY_HOST;
    private String RPC_QUEUE_REPLY_TO;
    private String RPC_QUEUE_SEND_TO = "load_balancer";

    public HTTPServerInitializer(int port) {
        RPC_QUEUE_REPLY_TO = port+"";
        establishConnection();
        serverQueue();
    }

    @Override
    protected void initChannel(SocketChannel arg0) {

        CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin()
                .allowedRequestHeaders("X-Requested-With", "Content-Type", "Content-Length")
                .allowedRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS)
                .build();
        ChannelPipeline p = arg0.pipeline();
        p.addLast("decoder", new HttpRequestDecoder());
        p.addLast("encoder", new HttpResponseEncoder());
        p.addLast(new CorsHandler(corsConfig));
        p.addLast(new HTTPHandler());
        p.addLast("MQ", new RequestHandler(senderChannel, uuid, RPC_QUEUE_REPLY_TO, RPC_QUEUE_SEND_TO));
//        p.addLast("aggregator",
//                new HttpObjectAggregator(512 * 1024));
//        p.addLast("request",new CustumHandler());

    }

    private void establishConnection() {
        factory = new ConnectionFactory();
        factory.setHost(FACTORY_HOST);
        Connection connection = null;
        try {
            connection = factory.newConnection();
            receiverChannel = connection.createChannel();
            senderChannel = connection.createChannel();
            senderChannel.queueDeclare(RPC_QUEUE_SEND_TO, false, false, false, null);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private void serverQueue() {
        try {
            receiverChannel.queueDeclare(RPC_QUEUE_REPLY_TO, false, false, false, null);
            receiverChannel.basicQos(1);
            Consumer consumer = new DefaultConsumer(receiverChannel) {
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

                    receiverChannel.basicAck(envelope.getDeliveryTag(), false);

                }
            };
            receiverChannel.basicConsume(RPC_QUEUE_REPLY_TO, false, consumer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

