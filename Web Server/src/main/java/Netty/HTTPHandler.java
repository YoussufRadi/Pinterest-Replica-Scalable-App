package Netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.netty.buffer.Unpooled.copiedBuffer;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HTTPHandler extends SimpleChannelInboundHandler<Object> {
    private HttpRequest request;
    private String requestBody;
    private long correlationId;
    volatile String responseBody;
    ExecutorService executorService = Executors.newCachedThreadPool();


    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
        ctx.fireChannelReadComplete();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println("HTTP Handler");
        JSONObject fullRequest = new JSONObject();

        if (msg instanceof HttpRequest) {
            HttpRequest request = this.request = (HttpRequest) msg;
            if (HttpHeaders.is100ContinueExpected(request)) {
                send100Continue(ctx);
            }

            fullRequest.put("version",request.protocolVersion());
            fullRequest.put("hostname",request.headers().get(HttpHeaderNames.HOST, "unknown"));
            fullRequest.put("uri",request.uri());

            HttpHeaders headers = request.headers();
            JSONObject headerJson = new JSONObject();
            if (!headers.isEmpty()) {
                for (Map.Entry<String, String> h: headers) {
                    CharSequence key = h.getKey();
                    CharSequence value = h.getValue();
                    headerJson.put(key, value);
                }
            }

            fullRequest.put("headers", headerJson);

            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
            Map<String, List<String>> params = queryStringDecoder.parameters();
            JSONObject paramJson = new JSONObject();
            if (!params.isEmpty()) {
                for (Map.Entry<String, List<String>> p: params.entrySet()) {
                    String key = p.getKey();
                    List<String> vals = p.getValue();
                    for (String val : vals) {
                        paramJson.put(key, val);
                    }
                }
            }
            fullRequest.put("parameters",paramJson);
            String requestId = UUID.randomUUID().toString();

            ctx.channel().attr(AttributeKey.valueOf("REQUEST")).set(fullRequest);
            ctx.channel().attr(AttributeKey.valueOf("CORRID")).set(requestId);

//            System.out.println(fullRequest.toString());
        }

        if (msg instanceof HttpContent) {
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();

            ctx.fireChannelRead(content);
        }
        if (msg instanceof LastHttpContent) {
//            LastHttpContent trailer = (LastHttpContent) msg;
            HttpObject trailer = (HttpObject) msg;
            if (!writeResponse(trailer, ctx)) {
                // If keep-alive is off, close the connection once the content is fully written.
                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
        }
    }


    private boolean writeResponse(HttpObject currentObj, ChannelHandlerContext ctx) {
        // Decide whether to close the connection or not.
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, currentObj.decoderResult().isSuccess()? OK : BAD_REQUEST,
                Unpooled.copiedBuffer("Not KeepAlive", CharsetUtil.UTF_8));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per:
            // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        // Write the response.
        ctx.write(response);

        return keepAlive;
    }


    private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}

