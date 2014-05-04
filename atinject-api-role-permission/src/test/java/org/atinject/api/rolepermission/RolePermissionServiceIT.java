package org.atinject.api.rolepermission;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class RolePermissionServiceIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(RolePermissionServiceIT.class);
    }
    
    @Inject
    private RolePermissionService rolePermissionService;
    
    @Test
    public void test() {
    	
    }
}
