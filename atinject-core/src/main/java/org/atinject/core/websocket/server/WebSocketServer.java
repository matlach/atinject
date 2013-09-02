package org.atinject.core.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.core.websocket.WebSocketEndpoint;
import org.atinject.core.websocket.server.event.WebSocketServerStarted;
import org.atinject.core.websocket.server.event.WebSocketServerStopped;
import org.slf4j.Logger;

@ApplicationScoped
@WebSocketEndpoint(path="/websocket")
public class WebSocketServer
{

    @Inject
    private Logger logger;
    
    @Inject @WebSocketEndpoint(path="/websocket")
    private Event<WebSocketServerStarted> webSocketServerStarted;
    
    @Inject @WebSocketEndpoint(path="/websocket")
    private Event<WebSocketServerStopped> webSocketServerStopped;

    @Inject @WebSocketEndpoint(path="/websocket")
    private WebSocketServerInitializer webSocketServerInitializer;
    
    private int port; // TODO inject a dedicated configuration object
    private ServerBootstrap b;
    private Channel ch;
    
    @PostConstruct
    public void initialize() throws Exception
    {
        // manage these features
        // Start servicing all request, vs. admin only,
        // Stop when work complete, force stop now, "stop" but continue admin request.
        // this should also work for any "tiers" components
        // that way it is possible to shutdown / test any system component live without impacting any non dependant features
        
        this.port = 8080;
        run();
    }
    
    @PreDestroy
    public void shutdown() throws Exception
    {
        try
        {
            if (ch != null)
            {
                ch.close().sync();
                webSocketServerStopped.fire(new WebSocketServerStopped());
            }
        }
        finally
        {
            if (b != null)
            {
                //b.shutdown();
            }
        }
    }
    
    public void run() throws Exception {
        b = new ServerBootstrap();
        b.group(new NioEventLoopGroup(), new NioEventLoopGroup())
         .channel(NioServerSocketChannel.class)
         .localAddress(port)
         .childHandler(webSocketServerInitializer.getInitializer());

        ch = b.bind().sync().channel();
        logger.info("Web socket server started at port {} ", port);
        logger.info("Open your browser and navigate to http://localhost: {}", port);
        webSocketServerStarted.fire(new WebSocketServerStarted());
    }
    
}
