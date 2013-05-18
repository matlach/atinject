package org.atinject.core.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class InjectableChannelInitializer extends ChannelInitializer<SocketChannel>
{

    public InjectableChannelInitializer(){
        super();
    }
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception
    {
        
    }

}
