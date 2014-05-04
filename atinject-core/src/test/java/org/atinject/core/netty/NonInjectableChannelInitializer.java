package org.atinject.core.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import org.atinject.core.cdi.CDI;

public class NonInjectableChannelInitializer extends ChannelInitializer<SocketChannel>
{
 
    InjectableChannelInitializerDelegate delegate;
    
    public NonInjectableChannelInitializer(){
        super();
        delegate = CDI.select(InjectableChannelInitializerDelegate.class).get();
        delegate.toString();
    }
    
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        delegate.initChannel(ch);
    }

}
