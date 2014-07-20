package org.atinject.integration;

import org.atinject.core.archive.PackageArchive;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class PackagingIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return createDefaultArchive(ArquillianIT.class);
    }
    
    @Test
    @InSequence(1)
    public void packageAtinject() throws Exception {
    	new PackageArchive().export();
    }
    
    @Test
    @InSequence(2)
    public void startAtInject() {
    	// TODO use common exec to start packaged .sh
    }
    
    @Test
    @InSequence(3)
    public void stopAtInject() {
    	// TODO use common exec to stop packaged .sh
    }
}
