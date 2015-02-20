package org.atinject.core.retry;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class RetryIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(RetryIT.class);
    }
    
    @Inject
    private ServiceWithRetry serviceWithRetry;
    
    @Test @InSequence(1)
    public void testThisShouldGetRetry() {
    	try {
    		serviceWithRetry.retry();
        	Assertions.failBecauseExceptionWasNotThrown(Exception.class);
    	}
    	catch (Exception e) {
    		Assertions.assertThat(e).isNotNull();
    	}
    }
}
