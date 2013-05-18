package org.atinject.api.websocket.server;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Specializes;

@Alternative @Specializes
@ApplicationScoped
public class WebSocketServerHandler extends org.atinject.core.websocket.server.WebSocketServerHandler {

    // this class distinguish the public and authoritive web socket server handler
}
