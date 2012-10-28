package org.atinject.core.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class WebSocketServer
{

    private final int port;

    public WebSocketServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        try {
            b.group(new NioEventLoopGroup(), new NioEventLoopGroup())
             .channel(NioServerSocketChannel.class)
                 .option(ChannelOption.TCP_NODELAY, true)
             .localAddress(port)
             .childHandler(new WebSocketServerInitializer());

            Channel ch = b.bind().sync().channel();
            System.out.println("Web socket server started at port " + port + '.');
            System.out.println("Open your browser and navigate to http://localhost:" + port + '/');

            ch.closeFuture().sync();
        } finally {
            b.shutdown();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new WebSocketServer(port).run();
    }
}
