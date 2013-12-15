package org.atinject.core.websocket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.atinject.core.websocket.server.event.WebSocketServerStarted;
import org.atinject.core.websocket.server.event.WebSocketServerStopped;
import org.slf4j.Logger;

@ApplicationScoped
public class WebSocketServer
{

    @Inject
    private Logger logger;
    
    @Inject
    private Event<WebSocketServerStarted> webSocketServerStarted;
    
    @Inject
    private Event<WebSocketServerStopped> webSocketServerStopped;

    private int port; // TODO inject a dedicated configuration object ?
    EventLoopGroup bossGroup;
    EventLoopGroup workerGroup;
    
    private ServerBootstrap b;
    private Channel ch;
    
    @PostConstruct
    public void initialize()
    {
        // manage these features
        // Start servicing all request, vs. admin only,
        // Stop when work complete, force stop now, "stop" but continue admin request.
        // this should also work for any "tiers" components
        // that way it is possible to shutdown / test any system component live without impacting any non dependant features
    }

    public void start() {
        port = 8080;
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
         .channel(NioServerSocketChannel.class)
         .childHandler(new DelegableWebSocketServerInitializer());

        ch = b.bind(port).syncUninterruptibly().channel();
        logger.info("Web socket server started at port {} ", port);
        logger.info("Open your browser and navigate to http://localhost:{}", port);
        webSocketServerStarted.fire(new WebSocketServerStarted());
    }
    
    public void stop() {
        if (ch != null) {
            ch.close().awaitUninterruptibly();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            webSocketServerStopped.fire(new WebSocketServerStopped());
        }
        
    }
    
}
