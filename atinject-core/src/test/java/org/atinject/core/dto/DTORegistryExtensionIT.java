package org.atinject.core.dto;

import javax.inject.Inject;

import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;

public class DTORegistryExtensionIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(DTORegistryExtensionIT.class);
    }
    
    @Inject
    private DTORegistryExtension extension;
    
    @Test @InSequence(1)
    public void testExtension() {
        Assert.assertTrue(extension.getClasses().contains(DummyDTO.class));
    }
}
