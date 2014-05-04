package org.atinject.integration;


import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.assertj.core.api.Assertions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class ArquillianIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(ArquillianIT.class);
    }

    @Inject
    private UserTransaction ut;
    
    @Test
    public void testArquillianBootstrap() {
    	Assertions.assertThat(ut).isNotNull();
    }
    
}
