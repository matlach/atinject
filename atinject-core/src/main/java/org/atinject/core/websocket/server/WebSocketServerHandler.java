package org.atinject.core.websocket.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
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
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.core.cdi.CDI;
import org.atinject.core.concurrent.AsynchronousService;
import org.atinject.core.dto.DTOObjectMapper;
import org.atinject.core.notification.NotificationEvent;
import org.atinject.core.session.Session;
import org.atinject.core.session.SessionContext;
import org.atinject.core.session.SessionFactory;
import org.atinject.core.session.SessionService;
import org.atinject.core.session.dto.SessionOpenedNotification;
import org.atinject.core.topology.TopologyService;
import org.atinject.core.websocket.WebSocketExtension;
import org.atinject.core.websocket.WebSocketExtension.WebSocketMessageMethod;
import org.atinject.core.websocket.dto.WebSocketNotification;
import org.atinject.core.websocket.dto.WebSocketRequest;
import org.atinject.core.websocket.dto.WebSocketResponse;
import org.infinispan.remoting.transport.TopologyAwareAddress;
import org.slf4j.Logger;

@ApplicationScoped
public class WebSocketServerHandler {

    @Inject
    private Logger logger;
    
    @Inject
    private WebSocketServerIndexPage webSocketServerIndexPage;
    
    @Inject
    private TopologyService topologyService;
    
    @Inject
    private WebSocketExtension webSocketExtension;
    
    private String WEBSOCKET_PATH = "/websocket";

    private static final AttributeKey<String> SESSION_ID_ATTRIBUTE_KEY = AttributeKey.valueOf("session");
    private static final AttributeKey<WebSocketServerHandshaker> WEB_SOCKET_SERVER_HANDSHAKER_KEY = AttributeKey.valueOf("handshaker");
    
    // TODO we must pass the executor from the initializer to here 
    private Map<String, Channel> channelGroup;
    
    @Inject
    private SessionService sessionService;
    
    @Inject
    private SessionFactory sessionFactory;
    
    @Inject
    private DTOObjectMapper dtoObjectMapper;
    
    @Inject
    private AsynchronousService asynchronousService;
    
    private int maximumConcurrentConnection;
    private boolean acceptConnection;
    private boolean processRequest;
    
    public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
    public static final int HTTP_CACHE_SECONDS = 60;
    
    @PostConstruct
    public void initialize() {
    	maximumConcurrentConnection = 1000;
        acceptConnection = true;
        processRequest = true;
    	channelGroup = new ConcurrentHashMap<>(maximumConcurrentConnection);
    }
    
    public void onNotificationEvent(@Observes NotificationEvent event){
        Channel channel = channelGroup.get(event.getSession().getSessionId());
        if (channel == null){
            // channel has been removed, cannot send notification, no-op
            return;
        }
        // send to websocket
        sendNotificationAsText(channel, event.getNotification());
    }
    
        public void channelRegistered(ChannelHandlerContext ctx) {
        	if (!acceptConnection) {
        		ctx.close();
        	}
        	if (channelGroup.size() > maximumConcurrentConnection) {
        		ctx.close();
        	}
            logger.info("channel registered");
        }
        
        public void channelActive(ChannelHandlerContext ctx) {
            logger.info("channel active");
        }
        
        public void messageReceived(ChannelHandlerContext ctx, Object msg) {
        	if (!processRequest) {
        		return;
        	}
            if (msg instanceof FullHttpRequest) {
                handleHttpRequest(ctx, (FullHttpRequest) msg);
            }
            else if (msg instanceof WebSocketFrame) {
                handleWebSocketFrame(ctx, (WebSocketFrame) msg);
            }
        }
        
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }
        
        // TODO this method could be upgraded to handle REST/JAX-RS request
        public void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
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

        public void handleHttpBadRequest(ChannelHandlerContext ctx, FullHttpRequest req){
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
        }
        
        public void handleHttpNonGetRequest(ChannelHandlerContext ctx, FullHttpRequest req){
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN));
        }
        
        public void handleHttpIndexPage(ChannelHandlerContext ctx, FullHttpRequest req){
            FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

            ByteBuf content = webSocketServerIndexPage.getContent(getWebSocketLocation(req));

            res.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
            HttpHeaders.setContentLength(res, content.readableBytes());

            res.content().writeBytes(content);
            sendHttpResponse(ctx, req, res);
        }
        
        public void handleHttpFavico(ChannelHandlerContext ctx, FullHttpRequest req){
            FullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND);
            sendHttpResponse(ctx, req, res);
        }
        
        public void handleHttpStaticContent(ChannelHandlerContext ctx, FullHttpRequest request) {
            try {
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
                try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
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
                } catch (FileNotFoundException fnfe) {
                    sendError(ctx, HttpResponseStatus.NOT_FOUND);
                    return;
                }
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        
        public String sanitizeUri(String uri) {
            String sanitizedURI = uri;
            // Decode the path.
            try {
                sanitizedURI = URLDecoder.decode(uri, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                try {
                    sanitizedURI = URLDecoder.decode(uri, "ISO-8859-1");
                } catch (UnsupportedEncodingException e1) {
                    throw new Error(e1);
                }
            }

            if (!sanitizedURI.startsWith("/")) {
                return null;
            }

            // Convert file separators.
            sanitizedURI = sanitizedURI.replace('/', File.separatorChar);

            // Simplistic dumb security check.
            // You will have to do something serious in the production environment.
            if (sanitizedURI.contains(File.separator + '.') ||
                sanitizedURI.contains('.' + File.separator) ||
                sanitizedURI.startsWith(".") || sanitizedURI.endsWith(".") ||
                sanitizedURI.contains("../")) {
                return null;
            }

            // Convert to absolute path.
            return System.getProperty("user.dir") + File.separator + uri;
        }
        
        public void sendNotModified(ChannelHandlerContext ctx) {
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_MODIFIED);
            setDateHeader(response);

            // Close the connection as soon as the error message is sent.
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        }

        public void setDateHeader(FullHttpResponse response) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
            dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

            Calendar time = new GregorianCalendar();
            response.headers().set(HttpHeaders.Names.DATE, dateFormatter.format(time.getTime()));
        }

        public void setDateAndCacheHeaders(FullHttpResponse response, File fileToCache) {
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

        public void setContentTypeHeader(FullHttpResponse response, File file) {
            MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
        }
        
        public void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, status);
            response.content().writeBytes(Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");

            // Close the connection as soon as the error message is sent.
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        }
        
        public void handleHttpWebSocketHandshake(ChannelHandlerContext ctx, FullHttpRequest req){
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
                Attribute<String> sessionAttribute = ctx.channel().attr(SESSION_ID_ATTRIBUTE_KEY);
                String sessionId = UUID.randomUUID().toString();
                Session session = sessionFactory.newSession()
                    .setSessionId(sessionId);
                TopologyAwareAddress address = topologyService.getLocalAddress();
                session.setMachineId(address.getMachineId());
                
                sessionAttribute.set(sessionId);
                channelGroup.put(sessionId, ctx.channel());
                sessionService.openSession(session);
                
                // send notification to provide session id to the client
                SessionOpenedNotification notification = new SessionOpenedNotification();
                notification.setSessionId(session.getSessionId());
                sendNotificationAsText(ctx.channel(), notification);
            }
        }
        
        public void handleHttpNotFound(ChannelHandlerContext ctx, FullHttpRequest req){
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
        }
        
        public void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
            // Generate an error page if response getStatus code is not OK (200).
            if (res.getStatus().code() != 200) {
                res.content().writeBytes(Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8));
                HttpHeaders.setContentLength(res, res.content().readableBytes());
            }

            // Send the response and close the connection if necessary.
            ChannelFuture f = ctx.channel().write(res);
            if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
                f.addListener(ChannelFutureListener.CLOSE);
            }
        }
        
        public void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
            if (frame instanceof CloseWebSocketFrame) {
                handleCloseWebSocketFrame(ctx, (CloseWebSocketFrame) frame);
                return;
            }
            if (frame instanceof PingWebSocketFrame) {
                handlePingWebSocketFrame(ctx, (PingWebSocketFrame) frame);
                return;
            }
            if (frame instanceof BinaryWebSocketFrame){
                return;
            }
            if (frame instanceof TextWebSocketFrame){
                handleTextWebSocketFrame(ctx, (TextWebSocketFrame) frame);
                return;
            }
            
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }
        
        public void handleCloseWebSocketFrame(ChannelHandlerContext ctx, CloseWebSocketFrame frame){
            Attribute<WebSocketServerHandshaker> handshakerAttribute = ctx.channel().attr(WEB_SOCKET_SERVER_HANDSHAKER_KEY);
            WebSocketServerHandshaker handshaker = handshakerAttribute.get();
            if (handshaker != null){
                handshaker.close(ctx.channel(), frame);
            }
        }
        
        public void handlePingWebSocketFrame(ChannelHandlerContext ctx, PingWebSocketFrame frame){
            ctx.channel().write(new PongWebSocketFrame(frame.content()));
        }
        
        public void handleTextWebSocketFrame(ChannelHandlerContext ctx, TextWebSocketFrame frame){
            // validate session (need to be bound at that point)
            Attribute<String> sessionAttribute = ctx.channel().attr(SESSION_ID_ATTRIBUTE_KEY);
            String sessionId = sessionAttribute.get();
            if (sessionId == null){
                throw new RuntimeException("handshake is not complete, kick");
            }
            
            // decode text web socket frame
            WebSocketRequest request = decodeTextWebSocketFrame(frame);
            
            // perform request
            WebSocketResponse response = performRequest(sessionId, request);
            
            // send response
            sendResponseAsText(ctx.channel(), response);
        }
        
        public WebSocketRequest decodeTextWebSocketFrame(TextWebSocketFrame frame){
            // read json
            final String json = frame.text();
            
            // unserialize json
            final WebSocketRequest request = dtoObjectMapper.readValue(json);
            
            return request;
        }
        
        public WebSocketResponse performRequest(String sessionId, WebSocketRequest request){
            // build task
            WebSocketMessageTask task = new WebSocketMessageTask()
                .setSessionId(sessionId)
                .setRequest(request);
            
            // should we use a future here to wait for response ?
            // response could be sent asynchronously instead ?
            // should we use completion service ?
            Future<WebSocketResponse> future;
            future = asynchronousService.submit(task);
            
            // wait for response to come back
            WebSocketResponse response = getWebSocketResponseFromFuture(future);
            
            // bind the original request id back in the response
            response.setRequestId(request.getRequestId());
            
            return response;
        }
        
        public class WebSocketMessageTask implements Callable<WebSocketResponse>{
            
            private String sessionId;
            private WebSocketRequest request;
            
            public String getSessionId() {
                return sessionId;
            }

            public WebSocketMessageTask setSessionId(String sessionId) {
                this.sessionId = sessionId;
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
            	SessionContext sessionContext = CDI.select(SessionContext.class).get();
                Session session = CDI.select(SessionService.class).get().getSession(sessionId).get();
                // set current session
                sessionContext.set(session);
                try {
                    // manually inject web socket extension, as callable should have been serialized
                    WebSocketExtension webSocketExtension = CDI.select(WebSocketExtension.class).get();
                    
                    WebSocketMessageMethod webSocketMessageMethod = webSocketExtension.getWebSocketMessageMethod(request.getClass());
                    
                    Object returnValue = null;
                    if (webSocketMessageMethod.isInjectSessionParameter()) {
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
                finally {
                    sessionContext.remove();
                }
            }
        }
        
        public WebSocketResponse getWebSocketResponseFromFuture(Future<WebSocketResponse> future){
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
        
        public ChannelFuture sendNotificationAsText(Channel channel, WebSocketNotification notification){
            ByteBuf byteBuf = Unpooled.buffer().writeBytes(dtoObjectMapper.writeValueAsBytes(notification));
            return channel.write(new TextWebSocketFrame(byteBuf));
        }
        
        public ChannelFuture sendResponseAsText(Channel channel, WebSocketResponse response){
            ByteBuf byteBuf = Unpooled.buffer().writeBytes(dtoObjectMapper.writeValueAsBytes(response));
            return channel.write(new TextWebSocketFrame(byteBuf));
        }
        
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.warn(cause.getMessage());
            ctx.close();
        }
        
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            logger.info("user event triggered '" + evt + "'");
        }

        public void channelInactive(ChannelHandlerContext ctx) {
            logger.info("channel inactive");
            
            Attribute<String> sessionAttribute = ctx.channel().attr(SESSION_ID_ATTRIBUTE_KEY);
            String sessionId = sessionAttribute.get();
            if (sessionId != null){
                Session session = sessionService.getSession(sessionId).get();
                // we should dispatch instead to "web socket close" method
                sessionService.closeSession(session);
            }
            
            channelGroup.remove(ctx.channel());
        }
        
        private String getWebSocketLocation(FullHttpRequest req) {
            return "ws://" + req.headers().get(HttpHeaders.Names.HOST) + WEBSOCKET_PATH;
        }
    
}
