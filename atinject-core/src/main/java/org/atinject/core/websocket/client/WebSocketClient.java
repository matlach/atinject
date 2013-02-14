package org.atinject.core.websocket.client;

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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.session.dto.SessionOpenedNotification;
import org.atinject.core.dto.BaseDTO;
import org.atinject.core.json.DTOObjectMapper;
import org.atinject.core.netty.ByteBufUtil;
import org.atinject.core.websocket.WebSocketEndpoint;
import org.atinject.core.websocket.dto.BaseWebSocketRequest;
import org.atinject.core.websocket.server.WebSocketServer;
import org.atinject.core.websocket.server.event.WebSocketServerStarted;
import org.atinject.core.websocket.server.event.WebSocketServerStopped;
import org.slf4j.Logger;

@ApplicationScoped
@WebSocketEndpoint(path="/websocket")
public class WebSocketClient {

    @Inject
    private Logger logger;
    
    @Inject @WebSocketEndpoint(path="/websocket")
    private WebSocketServer server;
    
    @Inject
    private DTOObjectMapper dtoObjectMapper;
    
    private String sessionId;
    private String userId;
    
    private URI uri;

    private Bootstrap b;
    private Channel ch;
    
    @PostConstruct
    public void initialize() throws Exception
    {
        uri = new URI("ws://localhost:8080/websocket");
    }
    
    @PreDestroy
    public void shutdown()
    {
        if (b != null)
        {
            b.shutdown();
        }
    }
    
    public String getSessionId(){
        return sessionId;
    }
    
    public String getUserId(){
        return userId;
    }
    
    public void onWebSocketServerStarted(
            @Observes @WebSocketEndpoint(path="/websocket") WebSocketServerStarted event) throws Exception{
        run();        
    }
    
    public void onWebSocketServerStopped(
            @Observes @WebSocketEndpoint(path="/websocket") WebSocketServerStopped event){
        if (b != null)
        {
            b.shutdown();
        }
    }

    public void run() throws Exception {
        b = new Bootstrap();

        String protocol = uri.getScheme();
        if (!protocol.equals("ws")) {
            throw new IllegalArgumentException("Unsupported protocol: " + protocol);
        }

        // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
        // If you change it to V00, ping is not supported and remember to change
        // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
        final WebSocketClientHandler handler =
                new WebSocketClientHandler(
                        new WebSocketClientHandshakerFactory().newHandshaker(
                                uri, WebSocketVersion.V13, null, false, null));

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

        logger.info("WebSocket Client connecting");
        ch = b.connect().sync().channel();
        handler.handshakeFuture().sync();
    }
    
    public ChannelFuture send(BaseWebSocketRequest request) throws Exception
    {
        ByteBuf byteBuf = Unpooled.buffer();
        while (sessionId == null){
            logger.info("waiting 1 sec to wait for handshake to complete, session is null");
            Thread.sleep(1000L);
        }
        
        // write session id
        ByteBufUtil.writeUTF8(byteBuf, sessionId);
        
        // write json
        ByteBufUtil.writeUTF8(byteBuf, dtoObjectMapper.writeValueAsString(request));
        
        return ch.write(new BinaryWebSocketFrame(byteBuf));
    }
    
    public void sendPing()
    {
     // Ping
        logger.info("WebSocket Client sending ping");
        ch.write(new PingWebSocketFrame(Unpooled.copiedBuffer(new byte[]{1, 2, 3, 4, 5, 6})));
    }
    
    public void sendClose() throws InterruptedException
    {
        // Close
        logger.info("WebSocket Client sending close");
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
            logger.info("WebSocket Client disconnected!");
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
            Channel ch = ctx.channel();
            if (!handshaker.isHandshakeComplete()) {
                handshaker.finishHandshake(ch, (HttpResponse) msg);
                logger.info("WebSocket Client connected!");
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
                logger.info("WebSocket Client received message: " + textFrame.getText());
            } else if (frame instanceof PongWebSocketFrame) {
                logger.info("WebSocket Client received pong");
            } else if (frame instanceof BinaryWebSocketFrame) {
                logger.info("WebSocket Client received binary ");
                handleBinaryWebSocketFrame(ctx, (BinaryWebSocketFrame) frame);
            } else if (frame instanceof CloseWebSocketFrame) {
                logger.info("WebSocket Client received closing");
                ch.close();
            }
        }
        
        private void handleBinaryWebSocketFrame(ChannelHandlerContext ctx, BinaryWebSocketFrame frame){
            ByteBuf byteBuf = frame.getBinaryData();
            
            String json = ByteBufUtil.readUTF8(byteBuf);
            BaseDTO dto = dtoObjectMapper.readValue(json);
            if (dto instanceof SessionOpenedNotification){
                SessionOpenedNotification notification = (SessionOpenedNotification) dto;
                sessionId = notification.getSessionId();
            }
//            else if(dto instanceof ){
//                
//            }
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