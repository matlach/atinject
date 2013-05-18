package org.atinject.core.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;

public class InjectableInboundMessageHandlerAdapter extends ChannelInboundMessageHandlerAdapter<Object>
{
//    public InjectableInboundMessageHandlerAdapter() {
//        super();
//    }
    
    @Override
    public void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        
    }

}
