package org.atinject.integration;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class ManualTesting extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(ArquillianIT.class);
    }
    
    @Test
    public void testManually() throws IOException {
        System.in.read();
    }
}
