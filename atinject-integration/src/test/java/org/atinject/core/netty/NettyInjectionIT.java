package org.atinject.core.netty;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;

public class NettyInjectionIT extends IntegrationTest
{
    @Inject private InjectableChannelInitializer injectableChannelInitializer;
    @Inject private InjectableInboundMessageHandlerAdapter injectableInboundMessageHandlerAdapter;
    
    @Test
    public void testInject(){
        Assert.assertNotNull(injectableChannelInitializer);
        Assert.assertNotNull(injectableInboundMessageHandlerAdapter);
    }
}
