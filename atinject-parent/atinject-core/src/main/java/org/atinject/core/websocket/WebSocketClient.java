package org.atinject.core.websocket;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;

import java.net.URI;
import java.util.HashMap;
import java.util.UUID;

import org.atinject.core.distexec.UserKey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;

public class WebSocketClient {

    private final URI uri;

    public WebSocketClient(URI uri) {
        this.uri = uri;
    }

    public void run() throws Exception {
        Bootstrap b = new Bootstrap();
        try {

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
            Channel ch = b.connect().sync().channel();
            handler.handshakeFuture().sync();

            
            SmileFactory f = new SmileFactory();
         // can configure instance with 'SmileParser.Feature' and 'SmileGenerator.Feature'
         ObjectMapper mapper = new ObjectMapper(f);
         // and then read/write data as usual
         UserKey value = new UserKey();
         value.setA(10);
         byte[] smileData = mapper.writeValueAsBytes(value);
         UserKey otherValue = mapper.readValue(smileData, UserKey.class);
         System.out.println(otherValue.getA());
            // Send 10 messages and wait for responses
            System.out.println("WebSocket Client sending message");
            for (int i = 0; i < 10; i++) {
                ch.write(new TextWebSocketFrame("Message #" + i));
                
                ch.write(new BinaryWebSocketFrame(Unpooled.copiedBuffer(UUID.randomUUID().toString().getBytes())));
            }

            // Ping
            System.out.println("WebSocket Client sending ping");
            ch.write(new PingWebSocketFrame(Unpooled.copiedBuffer(new byte[]{1, 2, 3, 4, 5, 6})));

            // Close
            System.out.println("WebSocket Client sending close");
            ch.write(new CloseWebSocketFrame());

            // WebSocketClientHandler will close the connection when the server
            // responds to the CloseWebSocketFrame.
            ch.closeFuture().sync();
        } finally {
            b.shutdown();
        }
    }

    public static void main(String[] args) throws Exception {
        URI uri;
        if (args.length > 0) {
            uri = new URI(args[0]);
        } else {
            uri = new URI("ws://localhost:8080/websocket");
        }
        new WebSocketClient(uri).run();
    }
}