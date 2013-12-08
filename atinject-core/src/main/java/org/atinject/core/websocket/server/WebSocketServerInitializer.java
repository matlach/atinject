package org.atinject.core.websocket.server;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WebSocketServerInitializer {
    
    DelegableWebSocketServerHandler delegableWebSocketServerHandler;
    DefaultEventExecutorGroup executor;
    
    @PostConstruct
    public void initialize(){
        delegableWebSocketServerHandler = new DelegableWebSocketServerHandler();
        executor = new DefaultEventExecutorGroup(Runtime.getRuntime().availableProcessors()*2);
    }
    
    public void initChannel(SocketChannel ch) throws Exception
    {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("decoder", new HttpRequestDecoder(4096, 8192, 8192, false)); // disable header validation
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast(executor, "handler", delegableWebSocketServerHandler);
    }
}
