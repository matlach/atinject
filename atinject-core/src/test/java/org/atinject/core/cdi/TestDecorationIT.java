package org.atinject.core.cdi;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class TestDecorationIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(TestDecorationIT.class);
    }
    
    @Inject
    private TestServiceInterface service;
    
    @Test @InSequence(1)
    public void testDecoration(){
        System.out.println(service.getString());
    }
}
