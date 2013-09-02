package org.atinject.core.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class InjectableInboundMessageHandlerAdapter extends SimpleChannelInboundHandler<Object>
{
//    public InjectableInboundMessageHandlerAdapter() {
//        super();
//    }
    
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object messages) throws Exception {
        
    }

}
