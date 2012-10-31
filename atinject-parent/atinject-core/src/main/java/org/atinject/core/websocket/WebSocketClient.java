package org.atinject.core.websocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.user.dto.GetUserRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebSocketEndpoint(uri="ws://localhost:8080/websocket")
@ApplicationScoped
public class WebSocketClient {

    @Inject @WebSocketEndpoint(uri="ws://localhost:8080/websocket")
    private WebSocketServer server;
    
    private URI uri;

    private Bootstrap b;
    private Channel ch;
    
    @PostConstruct
    public void initialize() throws Exception
    {
        server.toString();
        uri = new URI("ws://localhost:9080/websocket");
        run();
    }
    
    @PreDestroy
    public void shutdown()
    {
        b.shutdown();
    }

    public void run() throws Exception {
        b = new Bootstrap();

        String protocol = uri.getScheme();
        if (!protocol.equals("ws")) {
            throw new IllegalArgumentException("Unsupported protocol: " + protocol);
        }

        HashMap<String, String> customHeaders = new HashMap<String, String>();
        customHeaders.put("MyHeader", "MyValue");

        // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
        // If you change it to V00, ping is not supported and remember to change
        // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
        final WebSocketClientHandler handler =
                new WebSocketClientHandler(
                        new WebSocketClientHandshakerFactory().newHandshaker(
                                uri, WebSocketVersion.V13, null, false, customHeaders));

        b.group(new NioEventLoopGroup())
         .channel(NioSocketChannel.class)
         .remoteAddress(uri.getHost(), uri.getPort())
         .handler(new ChannelInitializer<SocketChannel>() {
             @Override
             public void initChannel(SocketChannel ch) throws Exception {
                 ChannelPipeline pipeline = ch.pipeline();
                 pipeline.addLast("decoder", new HttpResponseDecoder());
                 pipeline.addLast("encoder", new HttpRequestEncoder());
                 pipeline.addLast("ws-handler", handler);
             }
         });

        System.out.println("WebSocket Client connecting");
        ch = b.connect().sync().channel();
        handler.handshakeFuture().sync();
    }
    
    private ObjectMapper jsonMapper = new ObjectMapper();
    public void send(BaseWebSocketRequest request) throws Exception
    {
        // write uuid.
        byte[] uuidBytes = UUID.randomUUID().toString().getBytes();
        
        // write class
        byte[] classBytes = GetUserRequest.class.getCanonicalName().getBytes();
        
        // write json
        byte[] jsonBytes = jsonMapper.writeValueAsBytes(request);
        
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(uuidBytes.length);
        byteBuf.writeBytes(uuidBytes);
        byteBuf.writeInt(classBytes.length);
        byteBuf.writeBytes(classBytes);
        byteBuf.writeInt(jsonBytes.length);
        byteBuf.writeBytes(jsonBytes);
        
        ch.write(new BinaryWebSocketFrame(byteBuf));
    }
    
    public void sendPing()
    {
     // Ping
        System.out.println("WebSocket Client sending ping");
        ch.write(new PingWebSocketFrame(Unpooled.copiedBuffer(new byte[]{1, 2, 3, 4, 5, 6})));
    }
    
    public void sendClose() throws InterruptedException
    {
        // Close
        System.out.println("WebSocket Client sending close");
        ch.write(new CloseWebSocketFrame());

        // WebSocketClientHandler will close the connection when the server
        // responds to the CloseWebSocketFrame.
        ch.closeFuture().sync();
    }

    public class WebSocketClientHandler extends ChannelInboundMessageHandlerAdapter<Object> {

        private final WebSocketClientHandshaker handshaker;
        private ChannelFuture handshakeFuture;

        public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
            this.handshaker = handshaker;
        }

        public ChannelFuture handshakeFuture() {
            return handshakeFuture;
        }

        @Override
        public void beforeAdd(ChannelHandlerContext ctx) throws Exception {
            handshakeFuture = ctx.newFuture();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            handshaker.handshake(ctx.channel());
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("WebSocket Client disconnected!");
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
            Channel ch = ctx.channel();
            if (!handshaker.isHandshakeComplete()) {
                handshaker.finishHandshake(ch, (HttpResponse) msg);
                System.out.println("WebSocket Client connected!");
                handshakeFuture.setSuccess();
                return;
            }

            if (msg instanceof HttpResponse) {
                HttpResponse response = (HttpResponse) msg;
                throw new Exception("Unexpected HttpResponse (status=" + response.getStatus() + ", content="
                        + response.getContent().toString(CharsetUtil.UTF_8) + ")");
            }

            WebSocketFrame frame = (WebSocketFrame) msg;
            if (frame instanceof TextWebSocketFrame) {
                TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
                System.out.println("WebSocket Client received message: " + textFrame.getText());
            } else if (frame instanceof PongWebSocketFrame) {
                System.out.println("WebSocket Client received pong");
            } else if (frame instanceof CloseWebSocketFrame) {
                System.out.println("WebSocket Client received closing");
                ch.close();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();

            if (!handshakeFuture.isDone()) {
                handshakeFuture.setFailure(cause);
            }

            ctx.close();
        }
    }
    
}