package org.atinject.api.role;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class RoleIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(RoleIT.class);
    }
    
    @Inject
    private RoleService roleService;
    
    @Test @InSequence(1)
    public void testDummyRole() {
    	Assertions.assertThat(true).isTrue();
    }
}
