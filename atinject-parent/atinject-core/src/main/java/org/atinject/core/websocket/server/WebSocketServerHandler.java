package org.atinject.core.websocket.server;

import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
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
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.session.Session;
import org.atinject.api.session.SessionService;
import org.atinject.api.session.event.SessionClosed;
import org.atinject.api.session.event.SessionOpened;
import org.atinject.core.cdi.BeanManagerExtension;
import org.atinject.core.concurrent.AsynchronousService;
import org.atinject.core.distexec.TopologyService;
import org.atinject.core.distexec.UserKey;
import org.atinject.core.distexec.UserRequestDistributedExecutor;
import org.atinject.core.json.DTOObjectMapper;
import org.atinject.core.netty.ByteBufUtil;
import org.atinject.core.notification.NotificationEvent;
import org.atinject.core.websocket.WebSocketEndpoint;
import org.atinject.core.websocket.WebSocketExtension;
import org.atinject.core.websocket.WebSocketExtension.WebSocketMessageMethod;
import org.atinject.core.websocket.dto.BaseWebSocketRequest;
import org.atinject.core.websocket.dto.BaseWebSocketResponse;
import org.infinispan.remoting.transport.TopologyAwareAddress;
import org.slf4j.Logger;

@ApplicationScoped
@WebSocketEndpoint(path="/websocket")
public class WebSocketServerHandler {

    @Inject
    private Logger logger;
    
    @Inject @WebSocketEndpoint(path="/websocket")
    private WebSocketServerIndexPage webSocketServerIndexPage;
    
    @Inject
    private TopologyService topologyService;
    
    @Inject
    private UserRequestDistributedExecutor distributedExecutor;
    
    @Inject
    private WebSocketExtension webSocketExtension;
    
    private String WEBSOCKET_PATH = "/websocket";

    private WebSocketServerHandshaker handshaker;

    private ChannelInboundMessageHandlerAdapter<Object> handler;
    
    private static final AttributeKey<Session> SESSION_ATTRIBUTE_KEY = new AttributeKey<>("sessionId");
    
    private ChannelGroup channelGroup = new DefaultChannelGroup("websocket clients");
    
    @Inject
    private SessionService sessionService;
    
    @Inject
    private DTOObjectMapper dtoObjectMapper;
    
    @Inject
    private AsynchronousService asynchronousService;
    
    @PostConstruct
    public void initialize(){
        handler = new ChannelInboundMessageHandlerAdapterHolder();
    }
    
    public void onNotificationEvent(@Observes NotificationEvent event){
        Channel channel = channelGroup.find(event.getSession().getChannelId());
        if (channel == null){
            // channel has been removed, cannot send notification, no-op
            return;
        }
        // send to websocket
        
    }
    
    @Sharable
    public class ChannelInboundMessageHandlerAdapterHolder extends ChannelInboundMessageHandlerAdapter<Object>
    {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) {
            logger.info("channel registered");
        }
        
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            logger.info("channel active");
            channelGroup.add(ctx.channel());
        }
        
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
            // TODO this probably can be instantiated only once
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    getWebSocketLocation(req), null, false);
            // TODO meanwhile handshaker cannot be shared, should be bound to channel ?
            handshaker = wsFactory.newHandshaker(req);
            if (handshaker == null) {
                wsFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), req);
                
                // we should dispatch instead to "web socket open" method
                Attribute<Session> sessionAttribute = ctx.attr(SESSION_ATTRIBUTE_KEY);
                Session session = new Session();
                session.setChannelId(ctx.channel().id());
                session.setSessionId(UUID.randomUUID().toString());
                TopologyAwareAddress address = topologyService.getLocalAddress();
                session.setMachineId(address.getMachineId());
                session.setRackId(address.getRackId());
                session.setSiteId(address.getSiteId());
                sessionAttribute.set(session);
                SessionOpened sessionOpened = new SessionOpened();
                sessionOpened.setSession(session);
                sessionService.onSessionOpened(sessionOpened);
                
                // send notification to provide session id to the client
                // also, when user log in, depending his user id, provide the best server url depdending on is affinity
                // since all servers are public (as they act as a static http server / dynamic http server and application server), 
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
            Attribute<Session> sessionAttribute = ctx.attr(SESSION_ATTRIBUTE_KEY);
            Session session = sessionAttribute.get();
            if (session == null){
                // not authorized ?
                // TODO throw ?
            }
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
            
            // validate session
            Attribute<Session> sessionAttribute = ctx.channel().attr(SESSION_ATTRIBUTE_KEY);
            Session session = sessionAttribute.get();
            if (session == null){
                return;
            }
            if (! session.getSessionId().equals(uuid)){
                return;
            }
            
            // read raw json
            final String json = ByteBufUtil.readUTF8(byteBuf);
            
            // unserialize json
            final BaseWebSocketRequest request = null;//dtoObjectMapper.get().readValue(json, BaseWebSocketRequest.class);

            // build task
            Callable<BaseWebSocketResponse> task = new WebSocketMessageTask();
            
            Future<BaseWebSocketResponse> future;
            if (session.getUserId() == null){
                // perform locally through asynchronous service (no gain of submitting request on any member of the cluster ?)
                future = asynchronousService.submit(task);
            }
            else{
                // build user affinity based key
                UserKey key = new UserKey();
                key.setId(session.getUserId());
                // submit task to distributed executor with given key
                future = distributedExecutor.submit(task, key);
            }
            
            try{
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
                byteBuf.writeBytes(dtoObjectMapper.writeValueAsBytes(response));
                
                ctx.channel().write(new BinaryWebSocketFrame(byteBuf));
            }
            catch (Exception e){
                logger.info(e.getMessage());
            }
        }
        
        public class WebSocketMessageTask implements Callable<BaseWebSocketResponse>{
            
            private BaseWebSocketRequest request;
            private Session session;
            
            @Override
            public BaseWebSocketResponse call() throws Exception
            {
                WebSocketExtension webSocketExtension = BeanManagerExtension.getReference(WebSocketExtension.class);
                WebSocketMessageMethod m = webSocketExtension.getWebSocketMessageMethod(request.getClass());
                
                Object returnValue = null;
                if (m.isInjectSessionParameter())
                {
                    if (m.isWebSocketRequestFirstParameter())
                    {
                        returnValue = m.getWebSocketMessageMethod().invoke(m.getTarget(), request, session);
                    }
                    else
                    {
                        returnValue = m.getWebSocketMessageMethod().invoke(m.getTarget(), session, request);
                    }
                }
                else
                {
                    returnValue = m.getWebSocketMessageMethod().invoke(m.getTarget(), request);
                }
                return (BaseWebSocketResponse) returnValue;
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
        public void channelInactive(ChannelHandlerContext ctx) {
            logger.info("channel inactive");
            
            Attribute<Session> sessionAttribute = ctx.attr(SESSION_ATTRIBUTE_KEY);
            Session session = sessionAttribute.get();
            if (session != null){
                // we should dispatch instead to "web socket close" method
                SessionClosed sessionClosed = new SessionClosed();
                sessionClosed.setSession(session);
                sessionService.onSessionClosed(sessionClosed);
            }
            
            channelGroup.remove(ctx.channel());
        }
        
        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) {
            logger.info("channel unregistered");
        }

        private String getWebSocketLocation(HttpRequest req) {
            return "ws://" + req.getHeader(HttpHeaders.Names.HOST) + WEBSOCKET_PATH;
        }
    }
    
    public ChannelInboundMessageHandlerAdapter<Object> get(){
        return handler;
    }

}
