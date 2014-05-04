package org.atinject.core.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.atinject.core.cdi.CDI;

@Sharable
public class NonInjectableSimpleChannelInboundHandler extends SimpleChannelInboundHandler<Object>
{
    
    InjectableSimpleChannelInboundHandlerDelegate delegate;
    
    public NonInjectableSimpleChannelInboundHandler(){
        super();
        delegate = CDI.select(InjectableSimpleChannelInboundHandlerDelegate.class).get();
        delegate.toString();
    }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, Object messages) throws Exception {
        delegate.messageReceived(ctx, messages);
    }

}
