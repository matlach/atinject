package org.atinject.core.netty;

import io.netty.channel.ChannelHandlerContext;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InjectableSimpleChannelInboundHandlerDelegate
{

    public void channelRead0(ChannelHandlerContext ctx, Object messages) throws Exception {
        
    }
}
