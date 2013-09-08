package org.atinject.core.websocket.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

import org.atinject.core.cdi.CDI;

public class DelegableWebSocketServerInitializer extends ChannelInitializer<SocketChannel>{

    WebSocketServerInitializer delegate;
    
    public DelegableWebSocketServerInitializer() {
        super();
        delegate = CDI.select(WebSocketServerInitializer.class).get();
        delegate.toString();
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        delegate.initChannel(ch);
    }
}
