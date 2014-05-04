package org.atinject.api.registration;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class RegistrationServiceIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(RegistrationServiceIT.class);
    }
    
    @Inject
    private RegistrationService registrationService;
    
    @Test
    public void test() {
    	
    }
}
