package org.atinject.core.netty;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InjectableChannelInitializerDelegate
{
    NonInjectableSimpleChannelInboundHandler handler;
    
    @PostConstruct
    public void initialize(){
        handler = new NonInjectableSimpleChannelInboundHandler();
    }
    
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("codec-http", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("handler", handler);
    }
}
