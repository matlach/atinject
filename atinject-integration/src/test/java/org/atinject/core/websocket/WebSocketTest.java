package org.atinject.core.websocket;

import javax.inject.Inject;

import org.atinject.api.user.dto.GetUserRequest;
import org.atinject.core.websocket.client.WebSocketClient;
import org.atinject.core.websocket.server.WebSocketServer;
import org.atinject.integration.IntegrationTest;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

public class WebSocketTest extends IntegrationTest
{

    @Inject
    private Logger logger;
    
    @Inject @WebSocketEndpoint(path="/websocket")
    private WebSocketClient client;
    
    @Inject @WebSocketEndpoint(path="/websocket")
    private WebSocketServer server;
    
    @Ignore @Test
    public void testWebSockets() throws Exception
    {
        server.toString();
        
        Thread.sleep(1000L); // wait for session id
        
        client.send(new GetUserRequest());
        
        Thread.sleep(1000L);
    }
    
    @Ignore @Test
    public void testWebSocketServerExternally() throws Exception{
        server.toString();
        logger.info("test web socket server externally (ex : browser). press any key in console to kill unit test");
        System.in.read();
    }
}
