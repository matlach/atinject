package org.atinject.core.security;

import java.io.File;
import java.nio.file.Paths;

import javax.inject.Inject;

import org.atinject.integration.ArquillianIT;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;

public class PasswordDigesterIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        
        File logbackXmlFile = Paths.get("src/test/resources/default").resolve("logback.xml").toFile().getAbsoluteFile();
        File validationXmlFile = Paths.get("src/test/resources/default").resolve("validation.xml").toFile();
        File beansXmlFile = Paths.get("src/test/resources/default").resolve("beans.xml").toFile();
        File javaxEnterpriseInjectSpiExtensionFile = Paths.get("src/main/resources/default").resolve("javax.enterprise.inject.spi.Extension").toFile();
        
        JavaArchive archive = createArchive(ArquillianIT.class);
        addAtinjectCore(archive);
        archive
                //.addAsManifestResource(beansXmlFile, "beans.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource(validationXmlFile, "validation.xml")
                .addAsManifestResource(javaxEnterpriseInjectSpiExtensionFile, "services/javax.enterprise.inject.spi.Extension")
                .addAsResource(logbackXmlFile, "/logback.xml");
        
        return archive;
    }
    
    @Inject private SimplePasswordDigester passwordDigester;
    
    @Test
    public void testSHA1Digest(){
        // see wikipedia : http://en.wikipedia.org/wiki/SHA-1
        String digestedPassword = passwordDigester.digest("The quick brown fox jumps over the lazy dog");
        String expectedDigestedPassword = "2fd4e1c67a2d28fced849ee1bb76e7391b93eb12";
        Assert.assertEquals(expectedDigestedPassword, digestedPassword);
    }
}
