package org.atinject.core.websocket;

import javax.inject.Inject;

import org.atinject.api.user.dto.GetUserRequest;
import org.atinject.core.websocket.client.WebSocketClient;
import org.atinject.core.websocket.server.WebSocketServer;
import org.atinject.integration.WeldRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldRunner.class)
public class WebSocketTest 
{

    @Inject @WebSocketEndpoint(path="/websocket")
    private WebSocketClient client;
    
    @Inject @WebSocketEndpoint(path="/websocket")
    private WebSocketServer server;
    
    @Test
    public void testWebSockets() throws Exception
    {
        server.toString();
        client.send(new GetUserRequest());
        Thread.sleep(10000);
    }
    
}
