package org.atinject.core.retry;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class RetryIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(RetryIT.class);
    }
    
    @Inject
    private ServiceWithRetry serviceWithRetry;
    
    @Test(expected=Exception.class)
    public void testThisShouldGetRetry(){
        serviceWithRetry.retry();
    }
}
