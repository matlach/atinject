package org.atinject.core.websocket.server;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.startup.Startup;

@ApplicationScoped
@Startup
public class WebSocketServerBootstrap
{

    @Inject WebSocketServer server;
    
    @PostConstruct
    public void initialize() {
        server.start();
    }
    
    @PreDestroy
    public void shutdown() {
        server.stop();
    }
}
