package org.atinject.api.authorization;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class AuthorizationServiceIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(AuthorizationServiceIT.class);
    }
    
    @Inject
    private AuthorizationService AuthorizationService;
    
    @Test
    public void test() {
    	Assertions.assertThat(true).isTrue();
    }
}
