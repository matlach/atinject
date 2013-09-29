package org.atinject.integration;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.nio.file.Paths;

import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;

public class ArquillianIT extends IntegrationTest
{

    @Inject UserTransaction ut;
    
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
    
    @Test
    public void testArquillianBootstrap() {
        assertThat(ut, is(not(equalTo(null))));
    }
    
}
