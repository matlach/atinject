package org.atinject.api.permission;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;

public class PermissionIT extends IntegrationTest
{

    @Inject PermissionService permissionService;
    
    @Test
    public void testDummyPermission(){
        Assert.assertTrue(permissionService.isPermission(DummyPermission.DUMMY_PERMISSION));
    }
}
