package org.atinject.core.websocket.server;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.atinject.core.cdi.CDI;

@Sharable
public class DelegableWebSocketServerHandler extends SimpleChannelInboundHandler<Object>{

    WebSocketServerHandler delegate;
    
    public DelegableWebSocketServerHandler() {
        super();
        delegate = CDI.select(WebSocketServerHandler.class).get();
        delegate.toString();
    }
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        delegate.channelRegistered(ctx);
    }
    
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        delegate.channelActive(ctx);
    }
    
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) {
        delegate.channelRead0(ctx, msg);
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        delegate.channelReadComplete(ctx);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        delegate.exceptionCaught(ctx, cause);
    }
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        delegate.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        delegate.channelInactive(ctx);
    }
    
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        delegate.channelUnregistered(ctx);
    }
}
