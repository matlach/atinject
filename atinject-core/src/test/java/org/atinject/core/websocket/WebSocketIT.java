package org.atinject.core.websocket;

import javax.inject.Inject;

import org.atinject.core.cache.CacheExtension;
import org.atinject.core.transaction.InMemoryTransactionServices;
import org.atinject.core.websocket.client.WebSocketClient;
import org.atinject.core.websocket.server.WebSocketServer;
import org.atinject.integration.ArquillianIT;
import org.atinject.integration.DefaultDeployment;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

public class WebSocketIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
    	return new DefaultDeployment(ArquillianIT.class)
			.appendEmptyBeansXml()
			.appendJavaxEnterpriseInjectSpiExtension(CacheExtension.class)
			.appendOrgJBossWeldBootstrapApiService(InMemoryTransactionServices.class)
			.appendResource("arquillian-logback.xml", "logback.xml")
			.appendResource("arquillian-jgroups.xml", "jgroups.xml")
			.getArchive();
    }
    
    @Inject
    private Logger logger;
    
    @Inject
    private WebSocketClient client;
    
    @Inject
    private WebSocketServer server;
    
    @Test @InSequence(1)
    public void testWebSockets() throws Exception {
        Thread.sleep(1000L); // wait for session id
        
        //client.send(new GetUserRequest());
        client.send(null);
        
        Thread.sleep(1000L);
    }
    
    @Ignore @Test
    public void testWebSocketServerExternally() throws Exception {
        server.toString();
        logger.info("test web socket server externally (ex : browser). press any key in console to kill unit test");
        System.in.read();
    }
}
