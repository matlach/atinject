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

public class MessageDigesterIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        
        File logbackXmlFile = Paths.get("src/test/resources/default").resolve("logback.xml").toFile().getAbsoluteFile();
        File validationXmlFile = Paths.get("src/test/resources/default").resolve("validation.xml").toFile();
        File beansXmlFile = Paths.get("src/test/resources/default").resolve("beans.xml").toFile();
        File javaxEnterpriseInjectSpiExtensionFile = Paths.get("src/main/resources/default").resolve("javax.enterprise.inject.spi.Extension").toFile();
        
        JavaArchive archive = createArchive(ArquillianIT.class);
        addPackageAndItIsDependencies(archive, core);
        archive
                //.addAsManifestResource(beansXmlFile, "beans.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource(validationXmlFile, "validation.xml")
                .addAsManifestResource(javaxEnterpriseInjectSpiExtensionFile, "services/javax.enterprise.inject.spi.Extension")
                .addAsResource(logbackXmlFile, "/logback.xml");
        
        return archive;
    }
    
    @Inject private MD5Digester md5Digester;
    @Inject private SHA1Digester sha1Digester;
    @Inject private SHA256Digester sha256Digester;

    @Test
    public void testMD5Digest() {
        // see wikipedia : http://en.wikipedia.org/wiki/SHA-1
        String digestedPassword = md5Digester.digest("The quick brown fox jumps over the lazy dog");
        String expectedDigestedPassword = "9e107d9d372bb6826bd81d3542a419d6";
        Assert.assertEquals(expectedDigestedPassword, digestedPassword);
    }
    
    @Test
    public void testSHA1Digest() {
        // see wikipedia : http://en.wikipedia.org/wiki/SHA-1
        String digestedPassword = sha1Digester.digest("The quick brown fox jumps over the lazy dog");
        String expectedDigestedPassword = "2fd4e1c67a2d28fced849ee1bb76e7391b93eb12";
        Assert.assertEquals(expectedDigestedPassword, digestedPassword);
    }
    
    @Test
    public void testSHA256Digest() {
        // see wikipedia : http://en.wikipedia.org/wiki/SHA-256
        String digestedPassword = sha256Digester.digest("The quick brown fox jumps over the lazy dog");
        String expectedDigestedPassword = "d7a8fbb307d7809469ca9abcb0082e4f8d5651e46d3cdb762d02d0bf37c9e592";
        Assert.assertEquals(expectedDigestedPassword, digestedPassword);
    }
    
}
