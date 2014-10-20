package org.atinject.api.permission;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;

public class PermissionIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(PermissionIT.class);
    }
    
    @Inject PermissionService permissionService;
    
    @Test
    public void testDummyPermission(){
        Assert.assertTrue(true);
    }
}
