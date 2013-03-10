package org.atinject.core.websocket.server;

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
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
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
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.session.Session;
import org.atinject.api.session.SessionService;
import org.atinject.api.session.dto.SessionOpenedNotification;
import org.atinject.api.session.event.SessionClosed;
import org.atinject.api.session.event.SessionOpened;
import org.atinject.core.cdi.BeanManagerExtension;
import org.atinject.core.concurrent.AsynchronousService;
import org.atinject.core.distexec.TopologyService;
import org.atinject.core.distexec.UserRequestDistributedExecutor;
import org.atinject.core.dto.DTOObjectMapper;
import org.atinject.core.netty.ByteBufUtil;
import org.atinject.core.notification.NotificationEvent;
import org.atinject.core.websocket.WebSocketEndpoint;
import org.atinject.core.websocket.WebSocketExtension;
import org.atinject.core.websocket.WebSocketExtension.WebSocketMessageMethod;
import org.atinject.core.websocket.dto.WebSocketNotification;
import org.atinject.core.websocket.dto.WebSocketRequest;
import org.atinject.core.websocket.dto.WebSocketResponse;
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

    private ChannelInboundMessageHandlerAdapterHolder handler;
    
    private static final AttributeKey<Session> SESSION_ATTRIBUTE_KEY = new AttributeKey<>("session");
    private static final AttributeKey<WebSocketServerHandshaker> WEB_SOCKET_SERVER_HANDSHAKER_KEY = new AttributeKey<>("handshaker");
    
    private ChannelGroup channelGroup = new DefaultChannelGroup("websocket clients");
    
    @Inject
    private SessionService sessionService;
    
    @Inject
    private DTOObjectMapper dtoObjectMapper;
    
    @Inject
    private AsynchronousService asynchronousService;
    
    // maybe this could be stored per client
    private boolean binary = false;
    
    public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
    public static final int HTTP_CACHE_SECONDS = 60;
    
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
        if (binary){
            handler.sendNotificationAsBinary(channel, event.getNotification());
        }
        else{
            handler.sendNotificationAsText(channel, event.getNotification());
        }
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
            if (msg instanceof FullHttpRequest) {
                handleHttpRequest(ctx, (FullHttpRequest) msg);
                return;
            }
            
            if (msg instanceof WebSocketFrame) {
                handleWebSocketFrame(ctx, (WebSocketFrame) msg);
                return;
            }
        }
        
        // TODO this method could be upgraded to handle REST/JAX-RS request
        private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
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
            
            // handle file
            if (req.getUri().startsWith("/file")){
                handleHttpStaticContent(ctx, req);
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

        private void handleHttpBadRequest(ChannelHandlerContext ctx, FullHttpRequest req){
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
        }
        
        private void handleHttpNonGetRequest(ChannelHandlerContext ctx, FullHttpRequest req){
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
        }
        
        private void handleHttpIndexPage(ChannelHandlerContext ctx, FullHttpRequest req){
            FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

            ByteBuf content = webSocketServerIndexPage.getContent(getWebSocketLocation(req));

            res.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
            HttpHeaders.setContentLength(res, content.readableBytes());

            res.data().writeBytes(content);
            sendHttpResponse(ctx, req, res);
        }
        
        private void handleHttpFavico(ChannelHandlerContext ctx, FullHttpRequest req){
            FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
            sendHttpResponse(ctx, req, res);
        }
        
        private void handleHttpStaticContent(ChannelHandlerContext ctx, FullHttpRequest request){
            try
            {
                final String uri = request.getUri();
                final String path = sanitizeUri(uri);
                if (path == null) {
                    sendError(ctx, HttpResponseStatus.NOT_FOUND);
                    return;
                }
    
                File file = new File(path);
                if (file.isHidden() || !file.exists()) {
                    sendError(ctx, HttpResponseStatus.NOT_FOUND);
                    return;
                }
    
                if (file.isDirectory()) {
                    // make sure directory listing is forbidden
                    sendError(ctx, HttpResponseStatus.FORBIDDEN);
                    return;
                }
    
                if (!file.isFile()) {
                    sendError(ctx, HttpResponseStatus.FORBIDDEN);
                    return;
                }
    
                // Cache Validation
                String ifModifiedSince = request.headers().get(HttpHeaders.Names.IF_MODIFIED_SINCE);
                if (ifModifiedSince != null && !ifModifiedSince.isEmpty()) {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
                    Date ifModifiedSinceDate = dateFormatter.parse(ifModifiedSince);
    
                    // Only compare up to the second because the datetime format we send to the client
                    // does not have milliseconds
                    long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
                    long fileLastModifiedSeconds = file.lastModified() / 1000;
                    if (ifModifiedSinceDateSeconds == fileLastModifiedSeconds) {
                        sendNotModified(ctx);
                        return;
                    }
                }
    
                // TODO use mapped byte buffer here ? 
                RandomAccessFile raf;
                try {
                    raf = new RandomAccessFile(file, "r");
                } catch (FileNotFoundException fnfe) {
                    sendError(ctx, HttpResponseStatus.NOT_FOUND);
                    return;
                }
                long fileLength = raf.length();
    
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                HttpHeaders.setContentLength(response, fileLength);
                setContentTypeHeader(response, file);
                setDateAndCacheHeaders(response, file);
                if (HttpHeaders.isKeepAlive(request)) {
                    response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
                }
    
                // Write the initial line and the header.
                ctx.write(response);
    
                // Write the content.
                ChannelFuture writeFuture = ctx.write(new ChunkedFile(raf, 0, fileLength, 8192));
    
                // Decide whether to close the connection or not.
                if (!HttpHeaders.isKeepAlive(request)) {
                    // Close the connection when the whole content is written out.
                    writeFuture.addListener(ChannelFutureListener.CLOSE);
                }
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        
        private String sanitizeUri(String uri) {
            // Decode the path.
            try {
                uri = URLDecoder.decode(uri, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                try {
                    uri = URLDecoder.decode(uri, "ISO-8859-1");
                } catch (UnsupportedEncodingException e1) {
                    throw new Error();
                }
            }

            if (!uri.startsWith("/")) {
                return null;
            }

            // Convert file separators.
            uri = uri.replace('/', File.separatorChar);

            // Simplistic dumb security check.
            // You will have to do something serious in the production environment.
            if (uri.contains(File.separator + '.') ||
                uri.contains('.' + File.separator) ||
                uri.startsWith(".") || uri.endsWith(".") ||
                uri.contains("../")) {
                return null;
            }

            // Convert to absolute path.
            return System.getProperty("user.dir") + File.separator + uri;
        }
        
        private void sendNotModified(ChannelHandlerContext ctx) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_MODIFIED);
            setDateHeader(response);

            // Close the connection as soon as the error message is sent.
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        }

        private void setDateHeader(FullHttpResponse response) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
            dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

            Calendar time = new GregorianCalendar();
            response.headers().set(HttpHeaders.Names.DATE, dateFormatter.format(time.getTime()));
        }

        private void setDateAndCacheHeaders(FullHttpResponse response, File fileToCache) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
            dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

            // Date header
            Calendar time = new GregorianCalendar();
            response.headers().set(HttpHeaders.Names.DATE, dateFormatter.format(time.getTime()));

            // Add cache headers
            time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);
            response.headers().set(HttpHeaders.Names.EXPIRES, dateFormatter.format(time.getTime()));
            response.headers().set(HttpHeaders.Names.CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS);
            response.headers().set(HttpHeaders.Names.LAST_MODIFIED, dateFormatter.format(new Date(fileToCache.lastModified())));
        }

        private void setContentTypeHeader(FullHttpResponse response, File file) {
            MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
        }
        
        private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, status);
            response.data().writeBytes(Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");

            // Close the connection as soon as the error message is sent.
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        }
        
        private void handleHttpWebSocketHandshake(ChannelHandlerContext ctx, FullHttpRequest req){
            // TODO this probably can be instantiated only once
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    getWebSocketLocation(req), null, false);
            WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
            Attribute<WebSocketServerHandshaker> handshakerAttribute = ctx.channel().attr(WEB_SOCKET_SERVER_HANDSHAKER_KEY);
            handshakerAttribute.set(handshaker);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), req);
                
                // we should dispatch instead to "web socket open" method
                Attribute<Session> sessionAttribute = ctx.channel().attr(SESSION_ATTRIBUTE_KEY);
                Session session = new Session()
                    .setChannelId(ctx.channel().id())
                    .setSessionId(UUID.randomUUID().toString());
                TopologyAwareAddress address = topologyService.getLocalAddress();
                session.setMachineId(address.getMachineId())
                    .setRackId(address.getRackId())
                    .setSiteId(address.getSiteId());
                sessionAttribute.set(session);
                SessionOpened sessionOpened = new SessionOpened().setSession(session);
                sessionService.onSessionOpened(sessionOpened);
                
                // send notification to provide session id to the client
                SessionOpenedNotification notification = new SessionOpenedNotification();
                notification.setSessionId(session.getSessionId());
                if (binary){
                    sendNotificationAsBinary(ctx.channel(), notification);
                }
                else{
                    sendNotificationAsText(ctx.channel(), notification);
                }
            }
        }
        
        private void handleHttpNotFound(ChannelHandlerContext ctx, FullHttpRequest req){
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
        }
        
        private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
            // Generate an error page if response getStatus code is not OK (200).
            if (res.getStatus().code() != 200) {
                res.data().writeBytes(Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8));
                HttpHeaders.setContentLength(res, res.data().readableBytes());
            }

            // Send the response and close the connection if necessary.
            ChannelFuture f = ctx.channel().write(res);
            if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
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
                if (!binary){
                    throw new RuntimeException("server is not in binary mode");
                }
                handleBinaryWebSocketFrame(ctx, (BinaryWebSocketFrame) frame);
                return;
            }
            if (frame instanceof TextWebSocketFrame){
                if (binary){
                    throw new RuntimeException("server is not in text mode");
                }
                handleTextWebSocketFrame(ctx, (TextWebSocketFrame) frame);
                return;
            }
            
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }
        
        private void handleCloseWebSocketFrame(ChannelHandlerContext ctx, CloseWebSocketFrame frame){
            Attribute<WebSocketServerHandshaker> handshakerAttribute = ctx.channel().attr(WEB_SOCKET_SERVER_HANDSHAKER_KEY);
            WebSocketServerHandshaker handshaker = handshakerAttribute.get();
            if (handshaker != null){
                handshaker.close(ctx.channel(), frame);
            }
        }
        
        private void handlePingWebSocketFrame(ChannelHandlerContext ctx, PingWebSocketFrame frame){
            ctx.channel().write(new PongWebSocketFrame(frame.data()));
        }
        
        private void handleBinaryWebSocketFrame(ChannelHandlerContext ctx, BinaryWebSocketFrame frame){
            // validate session (need to be bound at that point)
            Attribute<Session> sessionAttribute = ctx.channel().attr(SESSION_ATTRIBUTE_KEY);
            Session session = sessionAttribute.get();
            if (session == null){
                throw new RuntimeException("handshake is not complete, kick");
            }
            
            // decode binary web socket frame
            WebSocketRequest request = decodeBinaryWebSocketFrame(frame);
            
            // perform request
            WebSocketResponse response = performRequest(session, request);
            
            // send response
            sendResponseAsBinary(ctx.channel(), response);
        }
        
        private void handleTextWebSocketFrame(ChannelHandlerContext ctx, TextWebSocketFrame frame){
            // validate session (need to be bound at that point)
            Attribute<Session> sessionAttribute = ctx.channel().attr(SESSION_ATTRIBUTE_KEY);
            Session session = sessionAttribute.get();
            if (session == null){
                throw new RuntimeException("handshake is not complete, kick");
            }
            
            // decode text web socket frame
            WebSocketRequest request = decodeTextWebSocketFrame(frame);
            
            // perform request
            WebSocketResponse response = performRequest(session, request);
            
            // send response
            sendResponseAsText(ctx.channel(), response);
        }
        
        private WebSocketRequest decodeBinaryWebSocketFrame(BinaryWebSocketFrame frame){
            // read json
            final String json = ByteBufUtil.readUTF8(frame.data());
            
            // unserialize json
            final WebSocketRequest request = dtoObjectMapper.readValue(json);
            
            return request;
        }
        
        private WebSocketRequest decodeTextWebSocketFrame(TextWebSocketFrame frame){
            // read json
            final String json = frame.text();
            
            // unserialize json
            final WebSocketRequest request = dtoObjectMapper.readValue(json);
            
            return request;
        }
        
        private WebSocketResponse performRequest(Session session, WebSocketRequest request){
            // build task
            WebSocketMessageTask task = new WebSocketMessageTask()
                .setSession(session)
                .setRequest(request);
            
            // should we use a future here to wait for response ?
            // response could be sent asynchronously instead ?
            // should we use completion service ?
            Future<WebSocketResponse> future;
            if (session.getUserId() == null){
                if (request.getRendezvous() == null){
                    // perform locally through asynchronous service (no gain of submitting request on any member of the cluster ?)
                    future = asynchronousService.submit(task);
                }
                else {
                    // submit task to distributed executor with given rendezvous
                    future = distributedExecutor.submit(task, session.getUserId());
                }
            }
            else{
                if (request.getRendezvous() == null){
                    // submit task to distributed executor using user id as rendezvous
                    future = distributedExecutor.submit(task, session.getUserId());
                }
                else {
                    // submit task to distributed executor with given rendezvous
                    future = distributedExecutor.submit(task, session.getUserId());
                }
            }
            
            // wait for response to come back
            WebSocketResponse response = getWebSocketResponseFromFuture(future);
            
            // bind the original request id back in the response
            response.setRequestId(request.getRequestId());
            
            return response;
        }
        
        public class WebSocketMessageTask implements Callable<WebSocketResponse>{
            
            private Session session;
            private WebSocketRequest request;
            
            public Session getSession() {
                return session;
            }

            public WebSocketMessageTask setSession(Session session) {
                this.session = session;
                return this;
            }
            
            public WebSocketRequest getRequest() {
                return request;
            }

            public WebSocketMessageTask setRequest(WebSocketRequest request) {
                this.request = request;
                return this;
            }

            @Override
            public WebSocketResponse call() throws Exception {
                // manually inject web socket extension, as callable should have been serialized
                WebSocketExtension webSocketExtension = BeanManagerExtension.getReference(WebSocketExtension.class);
                
                WebSocketMessageMethod webSocketMessageMethod = webSocketExtension.getWebSocketMessageMethod(request.getClass());
                
                Object returnValue = null;
                if (webSocketMessageMethod.isInjectSessionParameter())
                {
                    if (webSocketMessageMethod.isWebSocketRequestFirstParameter()) {
                        returnValue = webSocketMessageMethod.getWebSocketMessageMethod().invoke(webSocketMessageMethod.getTarget(), request, session);
                    }
                    else {
                        returnValue = webSocketMessageMethod.getWebSocketMessageMethod().invoke(webSocketMessageMethod.getTarget(), session, request);
                    }
                }
                else {
                    returnValue = webSocketMessageMethod.getWebSocketMessageMethod().invoke(webSocketMessageMethod.getTarget(), request);
                }
                return (WebSocketResponse) returnValue;
            }
        }
        
        private WebSocketResponse getWebSocketResponseFromFuture(Future<WebSocketResponse> future){
            try {
                return future.get();
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
            catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        
        private ChannelFuture sendNotificationAsBinary(Channel channel, WebSocketNotification notification){
            ByteBuf byteBuf = Unpooled.buffer();
            ByteBufUtil.writeUTF8(byteBuf, dtoObjectMapper.writeValueAsString(notification));
            return channel.write(new BinaryWebSocketFrame(byteBuf));
        }
        
        private ChannelFuture sendResponseAsBinary(Channel channel, WebSocketResponse response){
            ByteBuf byteBuf = Unpooled.buffer();
            ByteBufUtil.writeUTF8(byteBuf, dtoObjectMapper.writeValueAsString(response));
            return channel.write(new BinaryWebSocketFrame(byteBuf));
        }
        
        private ChannelFuture sendNotificationAsText(Channel channel, WebSocketNotification notification){
            ByteBuf byteBuf = Unpooled.buffer().writeBytes(dtoObjectMapper.writeValueAsBytes(notification));
            return channel.write(new TextWebSocketFrame(byteBuf));
        }
        
        private ChannelFuture sendResponseAsText(Channel channel, WebSocketResponse response){
            ByteBuf byteBuf = Unpooled.buffer().writeBytes(dtoObjectMapper.writeValueAsBytes(response));
            return channel.write(new TextWebSocketFrame(byteBuf));
        }
        
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.warn(cause.getMessage());
            ctx.close();
        }
        
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            logger.info("user event triggered '" + evt + "'");
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            logger.info("channel inactive");
            
            Attribute<Session> sessionAttribute = ctx.channel().attr(SESSION_ATTRIBUTE_KEY);
            Session session = sessionAttribute.get();
            if (session != null){
                // we should dispatch instead to "web socket close" method
                SessionClosed sessionClosed = new SessionClosed().setSession(session);
                sessionService.onSessionClosed(sessionClosed);
            }
            
            channelGroup.remove(ctx.channel());
        }
        
        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) {
            logger.info("channel unregistered");
        }

        private String getWebSocketLocation(FullHttpRequest req) {
            return "ws://" + req.headers().get(HttpHeaders.Names.HOST) + WEBSOCKET_PATH;
        }
    }
    
    public ChannelInboundMessageHandlerAdapter<Object> get(){
        return handler;
    }

}
