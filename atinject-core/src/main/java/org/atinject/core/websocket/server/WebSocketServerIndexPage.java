package org.atinject.core.websocket.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WebSocketServerIndexPage {

    public ByteBuf getContent(String webSocketLocation) {
        // TODO read templated index.html
        // TODO make remplacements
        // TODO return index.html
        return Unpooled.copiedBuffer("", CharsetUtil.UTF_8);
    }
}
