package org.atinject.core.websocket;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpChunkAggregator;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseEncoder;
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
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.atinject.api.session.Session;
import org.atinject.core.distexec.UserKey;
import org.atinject.core.distexec.UserRequestDistributedExecutor;
import org.atinject.core.websocket.WebSocketExtension.WebSocketMessageMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebSocketEndpoint(uri="ws://localhost:8080/websocket")
@ApplicationScoped
public class WebSocketServer
{

    @Inject
    private UserRequestDistributedExecutor distributedExecutor;
    
    @Inject
    private WebSocketExtension webSocketExtension;
    
    @Inject
    private BeanManager beanManager;
    
    ServerBootstrap b;
    Channel ch;
    
    private int port;

    @PostConstruct
    public void initialize() throws Exception
    {
        this.port = 9080;
        run();
    }
    
    @PreDestroy
    public void shutdown() throws Exception
    {
        ch.closeFuture().sync();
        b.shutdown();
    }
    
    public void run() throws Exception {
        b = new ServerBootstrap();
        b.group(new NioEventLoopGroup(), new NioEventLoopGroup())
         .channel(NioServerSocketChannel.class)
             .option(ChannelOption.TCP_NODELAY, true)
         .localAddress(port)
         .childHandler(new WebSocketServerInitializer());

        ch = b.bind().sync().channel();
        System.out.println("Web socket server started at port " + port + '.');
        System.out.println("Open your browser and navigate to http://localhost:" + port + '/');
    }
    
    public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        public void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("decoder", new HttpRequestDecoder());
            pipeline.addLast("aggregator", new HttpChunkAggregator(65536));
            pipeline.addLast("encoder", new HttpResponseEncoder());
            pipeline.addLast("handler", new WebSocketServerHandler());
        }
    }
    
    public class WebSocketServerHandler extends ChannelInboundMessageHandlerAdapter<Object> {
        private Logger logger = LoggerFactory.getLogger(WebSocketServerHandler.class);

        private String WEBSOCKET_PATH = "/websocket";

        private WebSocketServerHandshaker handshaker;

        @Override
        public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
            if (msg instanceof HttpRequest) {
                handleHttpRequest(ctx, (HttpRequest) msg);
            } else if (msg instanceof WebSocketFrame) {
                handleWebSocketFrame(ctx, (WebSocketFrame) msg);
            }
        }

        private void handleHttpRequest(ChannelHandlerContext ctx, HttpRequest req) throws Exception {
            // Handle a bad request.
            if (!req.getDecoderResult().isSuccess()) {
                sendHttpResponse(ctx, req, new DefaultHttpResponse(HTTP_1_1, BAD_REQUEST));
                return;
            }

            // Allow only GET methods.
            if (req.getMethod() != GET) {
                sendHttpResponse(ctx, req, new DefaultHttpResponse(HTTP_1_1, FORBIDDEN));
                return;
            }

            // Send the demo page and favicon.ico
            if (req.getUri().equals("/")) {
                HttpResponse res = new DefaultHttpResponse(HTTP_1_1, OK);

                ByteBuf content = WebSocketServerIndexPage.getContent(getWebSocketLocation(req));

                res.setHeader(CONTENT_TYPE, "text/html; charset=UTF-8");
                setContentLength(res, content.readableBytes());

                res.setContent(content);
                sendHttpResponse(ctx, req, res);
                return;
            } else if (req.getUri().equals("/favicon.ico")) {
                HttpResponse res = new DefaultHttpResponse(HTTP_1_1, NOT_FOUND);
                sendHttpResponse(ctx, req, res);
                return;
            }

            // Handshake
            WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    getWebSocketLocation(req), null, false);
            handshaker = wsFactory.newHandshaker(req);
            if (handshaker == null) {
                wsFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
            } else {
                handshaker.handshake(ctx.channel(), req);
            }
        }

        private ObjectMapper mapper = new ObjectMapper();
        private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

            // Check for closing frame
            if (frame instanceof CloseWebSocketFrame) {
                handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame);
                return;
            } else if (frame instanceof PingWebSocketFrame) {
                ctx.channel().write(new PongWebSocketFrame(frame.getBinaryData()));
                return;
            }
            else if (frame instanceof BinaryWebSocketFrame){
                frame.getBinaryData();
                
                // read uuid length
                int uuidBytesLength = frame.getBinaryData().readInt();
                
                // TODO validate length
                
                // read uuid
                final byte[] uuidBytes = new byte[uuidBytesLength];
                frame.getBinaryData().readBytes(uuidBytes);
                String uuid = new String(uuidBytes);
                
                // read class bytes length
                int classNameBytesLength = frame.getBinaryData().readInt();
                final byte[] classNameBytes = new byte[classNameBytesLength];
                frame.getBinaryData().readBytes(classNameBytes);
                
                // read json (remaining bytes)
                final byte[] jsonBytes = new byte[frame.getBinaryData().readableBytes()];
                frame.getBinaryData().readBytes(jsonBytes);
                
                // build key
                UserKey key = new UserKey();
                key.setUuid(uuid);
                
                // build task
                Callable<BaseWebSocketResponse> task = new Callable<BaseWebSocketResponse>(){
                    private byte[] jsonBytes;
                    
                    @Override
                    public BaseWebSocketResponse call() throws Exception
                    {
                        String className = new String(classNameBytes);
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
                    
                }

                ByteBuf byteBuf = Unpooled.buffer();
                try
                {
                    byteBuf.writeBytes(mapper.writeValueAsBytes(response));
                }
                catch (JsonProcessingException e)
                {
                    e.printStackTrace();
                }
                
                ctx.channel().write(new BinaryWebSocketFrame(byteBuf));
                
                System.out.println(uuid);
                return;
            } else if (!(frame instanceof TextWebSocketFrame)) {
                throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass()
                        .getName()));
            }

            // Send the uppercase string back.
            String request = ((TextWebSocketFrame) frame).getText();
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Channel %s received %s", ctx.channel().id(), request));
            }
            ctx.channel().write(new TextWebSocketFrame(request.toUpperCase()));
        }

        private void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
            // Generate an error page if response status code is not OK (200).
            if (res.getStatus().getCode() != 200) {
                res.setContent(Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8));
                setContentLength(res, res.getContent().readableBytes());
            }

            // Send the response and close the connection if necessary.
            ChannelFuture f = ctx.channel().write(res);
            if (!isKeepAlive(req) || res.getStatus().getCode() != 200) {
                f.addListener(ChannelFutureListener.CLOSE);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

        private String getWebSocketLocation(HttpRequest req) {
            return "ws://" + req.getHeader(HttpHeaders.Names.HOST) + WEBSOCKET_PATH;
        }
    }
    
    public static final class WebSocketServerIndexPage {

        private static final String NEWLINE = "\r\n";

        public static ByteBuf getContent(String webSocketLocation) {
            return Unpooled.copiedBuffer(
                    "<html><head><title>Web Socket Test</title></head>" + NEWLINE +
                    "<body>" + NEWLINE +
                    "<script type=\"text/javascript\">" + NEWLINE +
                    "var socket;" + NEWLINE +
                    "if (!window.WebSocket) {" + NEWLINE +
                    "  window.WebSocket = window.MozWebSocket;" + NEWLINE +
                    "}" + NEWLINE +
                    "if (window.WebSocket) {" + NEWLINE +
                    "  socket = new WebSocket(\"" + webSocketLocation + "\");" + NEWLINE +
                    "  socket.onmessage = function(event) {" + NEWLINE +
                    "    var ta = document.getElementById('responseText');" + NEWLINE +
                    "    ta.value = ta.value + '\\n' + event.data" + NEWLINE +
                    "  };" + NEWLINE +
                    "  socket.onopen = function(event) {" + NEWLINE +
                    "    var ta = document.getElementById('responseText');" + NEWLINE +
                    "    ta.value = \"Web Socket opened!\";" + NEWLINE +
                    "  };" + NEWLINE +
                    "  socket.onclose = function(event) {" + NEWLINE +
                    "    var ta = document.getElementById('responseText');" + NEWLINE +
                    "    ta.value = ta.value + \"Web Socket closed\"; " + NEWLINE +
                    "  };" + NEWLINE +
                    "} else {" + NEWLINE +
                    "  alert(\"Your browser does not support Web Socket.\");" + NEWLINE +
                    "}" + NEWLINE +
                    NEWLINE +
                    "function send(message) {" + NEWLINE +
                    "  if (!window.WebSocket) { return; }" + NEWLINE +
                    "  if (socket.readyState == WebSocket.OPEN) {" + NEWLINE +
                    "    socket.send(message);" + NEWLINE +
                    "  } else {" + NEWLINE +
                    "    alert(\"The socket is not open.\");" + NEWLINE +
                    "  }" + NEWLINE +
                    "}" + NEWLINE +
                    "</script>" + NEWLINE +
                    "<form onsubmit=\"return false;\">" + NEWLINE +
                    "<input type=\"text\" name=\"message\" value=\"Hello, World!\"/>" +
                    "<input type=\"button\" value=\"Send Web Socket Data\"" + NEWLINE +
                    "       onclick=\"send(this.form.message.value)\" />" + NEWLINE +
                    "<h3>Output</h3>" + NEWLINE +
                    "<textarea id=\"responseText\" style=\"width:500px;height:300px;\"></textarea>" + NEWLINE +
                    "</form>" + NEWLINE +
                    "</body>" + NEWLINE +
                    "</html>" + NEWLINE, CharsetUtil.US_ASCII);
        }

        private WebSocketServerIndexPage() {
            // Unused
        }
    }
}
