package org.atinject.core.websocket.server;

import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.session.Session;
import org.atinject.core.distexec.UserKey;
import org.atinject.core.distexec.UserRequestDistributedExecutor;
import org.atinject.core.json.JSon;
import org.atinject.core.netty.ByteBufUtil;
import org.atinject.core.websocket.WebSocketEndpoint;
import org.atinject.core.websocket.WebSocketExtension;
import org.atinject.core.websocket.WebSocketExtension.WebSocketMessageMethod;
import org.atinject.core.websocket.dto.BaseWebSocketRequest;
import org.atinject.core.websocket.dto.BaseWebSocketResponse;
import org.slf4j.Logger;

@ApplicationScoped
@WebSocketEndpoint(path="/websocket")
public class WebSocketServerHandler {

    @Inject
    private Logger logger;
    
    @Inject @WebSocketEndpoint(path="/websocket")
    private WebSocketServerIndexPage webSocketServerIndexPage;
    
    @Inject
    private UserRequestDistributedExecutor distributedExecutor;
    
    @Inject
    private WebSocketExtension webSocketExtension;
    
    private String WEBSOCKET_PATH = "/websocket";

    private WebSocketServerHandshaker handshaker;

    private ChannelInboundMessageHandlerAdapter<Object> handler;
    
    @PostConstruct
    public void initialize(){
        handler = new ChannelInboundMessageHandlerAdapter<Object>(){
            @Override
            public void messageReceived(ChannelHandlerContext ctx, Object msg) {
                if (msg instanceof HttpRequest) {
                    handleHttpRequest(ctx, (HttpRequest) msg);
                    return;
                }
                
                if (msg instanceof WebSocketFrame) {
                    handleWebSocketFrame(ctx, (WebSocketFrame) msg);
                    return;
                }
            }
            
            // TODO this method could be upgraded to handle REST/JAX-RS request
            private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req) {
                // Handle a bad request.
                if (!req.getDecoderResult().isSuccess()) {
                    handleHttpBadRequest(ctx, req);
                    return;
                }

                // Allow only GET methods.
                if (req.getMethod() != HttpMethod.GET) {
                    handleHttpNonGetRequest(ctx, req);
                    return;
                }

                // handle index
                if (req.getUri().equals("/")) {
                    handleHttpIndexPage(ctx, req);
                    return;
                }
                
                // send favicon.ico
                if (req.getUri().equals("/favicon.ico")) {
                    handleHttpFavico(ctx, req);
                    return;
                }

                // handle Handshake
                if (req.getUri().equals("/websocket")){
                    handleHttpWebSocketHandshake(ctx, req);
                    return;
                }
                
                // handle not found
                handleHttpNotFound(ctx, req);
            }

            private void handleHttpBadRequest(ChannelHandlerContext ctx, HttpRequest req){
                sendHttpResponse(ctx, req, new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            }
            
            private void handleHttpNonGetRequest(ChannelHandlerContext ctx, HttpRequest req){
                sendHttpResponse(ctx, req, new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
            }
            
            private void handleHttpIndexPage(ChannelHandlerContext ctx, HttpRequest req){
                HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

                ByteBuf content = webSocketServerIndexPage.getContent(getWebSocketLocation(req));

                res.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
                setContentLength(res, content.readableBytes());

                res.setContent(content);
                sendHttpResponse(ctx, req, res);
            }
            
            private void handleHttpFavico(ChannelHandlerContext ctx, HttpRequest req){
                HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
                sendHttpResponse(ctx, req, res);
            }
            
            private void handleHttpWebSocketHandshake(ChannelHandlerContext ctx, HttpRequest req){
                WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                        getWebSocketLocation(req), null, false);
                handshaker = wsFactory.newHandshaker(req);
                if (handshaker == null) {
                    wsFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
                } else {
                    handshaker.handshake(ctx.channel(), req);
                }
            }
            
            private void handleHttpNotFound(ChannelHandlerContext ctx, HttpRequest req){
                sendHttpResponse(ctx, req, new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
            }
            
            private void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
                // Generate an error page if response status code is not OK (200).
                if (res.getStatus().getCode() != 200) {
                    res.setContent(Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8));
                    setContentLength(res, res.getContent().readableBytes());
                }

                // Send the response and close the connection if necessary.
                ChannelFuture f = ctx.channel().write(res);
                if (!HttpHeaders.isKeepAlive(req) || res.getStatus().getCode() != 200) {
                    f.addListener(ChannelFutureListener.CLOSE);
                }
            }
            
            private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

                if (frame instanceof CloseWebSocketFrame) {
                    handleCloseWebSocketFrame(ctx, (CloseWebSocketFrame) frame);
                    return;
                }
                if (frame instanceof PingWebSocketFrame) {
                    handlePingWebSocketFrame(ctx, (PingWebSocketFrame) frame);
                    return;
                }
                if (frame instanceof BinaryWebSocketFrame){
                    handleBinaryWebSocketFrame(ctx, (BinaryWebSocketFrame) frame);
                    return;
                }
                if (frame instanceof TextWebSocketFrame){
                    handleTextWebSocketFrame(ctx, (TextWebSocketFrame) frame);
                    return;
                }
                
                throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
            }
            
            private void handleCloseWebSocketFrame(ChannelHandlerContext ctx, CloseWebSocketFrame frame){
                handshaker.close(ctx.channel(), frame);
            }
            
            private void handlePingWebSocketFrame(ChannelHandlerContext ctx, PingWebSocketFrame frame){
                ctx.channel().write(new PongWebSocketFrame(frame.getBinaryData()));
            }
            
            private void handleBinaryWebSocketFrame(ChannelHandlerContext ctx, BinaryWebSocketFrame frame){
                ByteBuf byteBuf = frame.getBinaryData();
                
                // read uuid
                final String uuid = ByteBufUtil.readUTF8(byteBuf);
                
                // read class
                final String className = ByteBufUtil.readUTF8(byteBuf);
                
                // read json
                final String json = ByteBufUtil.readUTF8(byteBuf);
                
                // build key
                UserKey key = new UserKey();
                key.setUuid(uuid);
                
                // build task
                Callable<BaseWebSocketResponse> task = new Callable<BaseWebSocketResponse>(){
                    private byte[] jsonBytes;
                    
                    @Override
                    public BaseWebSocketResponse call() throws Exception
                    {
                        Class<? extends BaseWebSocketRequest> request = (Class<? extends BaseWebSocketRequest>) Class.forName(className);
                        WebSocketMessageMethod m = webSocketExtension.getWebSocketMessageMethod(request);
                        
                        Object returnValue = null;
                        if (m.isInjectSessionParameter())
                        {
                            if (m.isWebSocketRequestFirstParameter())
                            {
                                returnValue = m.getWebSocketMessageMethod().invoke(m.getTarget(), request.newInstance(), Session.class.newInstance());
                            }
                            else
                            {
                                returnValue = m.getWebSocketMessageMethod().invoke(m.getTarget(), Session.class.newInstance(), request.newInstance());
                            }
                        }
                        else
                        {
                            returnValue = m.getWebSocketMessageMethod().invoke(m.getTarget(), request.newInstance());
                        }
                        return (BaseWebSocketResponse) returnValue;
                    }
                };
                
                // submit task to distributed executor with given key
                try{
                    Future<BaseWebSocketResponse> future = distributedExecutor.submit(task, key);
                    
                    BaseWebSocketResponse response = null;
                    try
                    {
                        response = future.get();
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                    }
                    catch (ExecutionException e)
                    {
                        e.printStackTrace();
                    }
                    byteBuf = Unpooled.buffer();
                    byteBuf.writeBytes(JSon.writeValueAsBytes(response));
                    
                    ctx.channel().write(new BinaryWebSocketFrame(byteBuf));
                }
                catch (Exception e){
                    logger.info(e.getMessage());
                }
            }
            
            private void handleTextWebSocketFrame(ChannelHandlerContext ctx, TextWebSocketFrame frame){
                // Send the uppercase string back.
                String request = frame.getText();
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("Channel %s received %s", ctx.channel().id(), request));
                }
                ctx.channel().write(new TextWebSocketFrame(request.toUpperCase()));
            }
            
            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                logger.info(cause.getMessage());
                ctx.close();
            }
            
            @Override
            public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
                    throws Exception {
                logger.info("user event triggered '" + evt + "'");
            }

            @Override
            public void channelRegistered(ChannelHandlerContext ctx) {
                logger.info("channel registered");
            }

            @Override
            public void channelUnregistered(ChannelHandlerContext ctx) {
                logger.info("channel unregistered");
            }

            @Override
            public void channelActive(ChannelHandlerContext ctx) {
                logger.info("channel active");
            }

            @Override
            public void channelInactive(ChannelHandlerContext ctx) {
                logger.info("channel inactive");
            }

            private String getWebSocketLocation(HttpRequest req) {
                return "ws://" + req.getHeader(HttpHeaders.Names.HOST) + WEBSOCKET_PATH;
            }
        };
    }
    
    public ChannelInboundMessageHandlerAdapter<Object> get(){
        return handler;
    }

}
