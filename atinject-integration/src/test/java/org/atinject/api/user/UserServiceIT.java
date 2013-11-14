package org.atinject.api.user;

import java.io.File;
import java.nio.file.Paths;

import javax.inject.Inject;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.integration.ArquillianIT;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

public class UserServiceIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        
        File logbackXmlFile = Paths.get("src/test/resources/default").resolve("logback.xml").toFile().getAbsoluteFile();
        File validationXmlFile = Paths.get("src/test/resources/default").resolve("validation.xml").toFile();
        File beansXmlFile = Paths.get("src/test/resources/default").resolve("beans.xml").toFile();
        File javaxEnterpriseInjectSpiExtensionFile = Paths.get("src/main/resources/default").resolve("javax.enterprise.inject.spi.Extension").toFile();
        
        JavaArchive archive = createArchive(ArquillianIT.class);
        addPackageAndItIsDependencies(archive, api_user);
        
        archive
                .addAsManifestResource(beansXmlFile, "beans.xml")
            //.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource(validationXmlFile, "validation.xml")
                .addAsManifestResource(javaxEnterpriseInjectSpiExtensionFile, "services/javax.enterprise.inject.spi.Extension")
                .addAsResource(logbackXmlFile, "/logback.xml");
        
        return archive;
    }
    
    @Inject
    private Logger logger;

    @Inject
    private UserService userService;
    
    @Test
    public void testAddGetUpdateGetRemoveUser()
    {
        logger.info("add");
        UserEntity user = userService.addUser("123");
        Assert.assertNotNull(user);

        logger.info("get");
        user = userService.getUser(user.getId());
        Assert.assertNotNull(user);

        logger.info("update");
        user.setName("456");
        userService.updateUser(user);
        
        logger.info("get updated");
        user = userService.getUser(user.getId());
        
        logger.info("remove");
        userService.removeUser(user);
        
        user = userService.getUser(user.getId());
        
        Assert.assertNull(user);
    }
}
