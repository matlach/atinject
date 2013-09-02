package org.atinject.core.dto;

import javax.inject.Inject;

import org.atinject.api.user.dto.User;
import org.atinject.integration.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;

public class DTORegistryExtensionIT extends IntegrationTest
{

    @Inject DTORegistryExtension extension;
    
    @Test
    public void testExtension()
    {
        Assert.assertTrue(extension.getClasses().contains(User.class));
    }
}
