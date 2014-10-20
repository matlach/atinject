package org.atinject.api.role;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;

public class RoleIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(RoleIT.class);
    }
    
    @Inject
    private RoleService roleService;
    
    @Test
    public void testDummyRole() {
        Assert.assertTrue(true);
    }
}
