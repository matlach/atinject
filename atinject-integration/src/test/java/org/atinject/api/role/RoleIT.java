package org.atinject.api.role;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;

public class RoleIT extends IntegrationTest
{

    @Inject RoleService roleService;
    
    @Test
    public void testDummyRole() {
        Assert.assertTrue(roleService.isRole(DummyRole.DUMMY_ROLE));
    }
}
