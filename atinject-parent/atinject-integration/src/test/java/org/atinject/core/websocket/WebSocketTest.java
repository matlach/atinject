package org.atinject.core.websocket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.inject.Inject;

import org.atinject.integration.WeldRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(WeldRunner.class)
public class WebSocketTest 
{

    @Inject @WebSocketEndpoint(uri="ws://localhost:8080/websocket")
    private WebSocketClient client;
    
    @Inject @WebSocketEndpoint(uri="ws://localhost:8080/websocket")
    private WebSocketServer server;
    
    @Test
    public void testWebSockets() throws Exception
    {
        //server.toString();
        //client.send(new GetUserRequest());
    }
    
    @Test
    public void testInputOutputStream() throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF("abc");
        dos.writeUTF("def");
        dos.close();
        
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()));
        System.out.println(dis.readUTF());
        System.out.println(dis.readUTF());
        dis.close();
    }
}
