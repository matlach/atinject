package org.atinject.api.user;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

import javax.inject.Inject;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.junit.Assert;
import org.junit.Test;

public class UserServiceIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        
        File logbackXmlFile = Paths.get("src/test/resources/default").resolve("logback.xml").toFile().getAbsoluteFile();
        File validationXmlFile = Paths.get("src/test/resources/default").resolve("validation.xml").toFile();
        File javaxEnterpriseInjectSpiExtensionFile = Paths.get("src/main/resources/default").resolve("javax.enterprise.inject.spi.Extension").toFile();

        BeansDescriptor beans = Descriptors.create(BeansDescriptor.class)
                .getOrCreateAlternatives()
//                    .clazz("org.atinject.api.usersession.UserSessionCache")
//                    .clazz("org.atinject.api.usersession.UserSessionFactory")
//                    .clazz("org.atinject.api.usersession.UserSessionService")
                    .up()
                .getOrCreateDecorators()
                    .up()
                .getOrCreateInterceptors()
                    .clazz("org.atinject.core.concurrent.AsynchronousInterceptor")
                    .clazz("org.atinject.core.concurrent.RetryInterceptor")
                    .clazz("org.atinject.core.profiling.ProfileInterceptor")
                    .clazz("org.atinject.core.thread.ThreadTrackerInterceptor")
                    .clazz("org.atinject.core.tiers.exception.HandleWebSocketServiceExceptionInterceptor")
                    .clazz("org.atinject.core.tiers.exception.HandleServiceExceptionInterceptor")
                    .clazz("org.atinject.core.transaction.TransactionalInterceptor")
                    .up();
        
        JavaArchive archive = createArchive(UserServiceIT.class);
        addPackageAndItIsDependencies(archive, api_user);
        
        archive
//                .addAsManifestResource(beansXmlFile, "beans.xml")
                .addAsManifestResource(new StringAsset(beans.exportAsString()), "beans.xml")
                .addAsManifestResource(validationXmlFile, "validation.xml")
                .addAsManifestResource(javaxEnterpriseInjectSpiExtensionFile, "services/javax.enterprise.inject.spi.Extension")
                .addAsResource(logbackXmlFile, "/logback.xml");
        
        return archive;
    }
    
    @Inject
    private UserService userService;
    
    private static UUID userId;
    
    @Test
    @InSequence(1)
    public void testAdd() {
        UserEntity user = userService.addUser("123");
        userId = user.getId();
    }
    
    @Test
    @InSequence(2)
    public void testGet() {
        UserEntity user = userService.getUser(userId);
        Assert.assertNotNull(user);
    }
    
    @Test
    @InSequence(3)
    public void testUpdate() {
        UserEntity user = userService.getUser(userId); 
        user.setName("456");
        userService.updateUser(user);
        
        user = userService.getUser(userId);
        Assert.assertEquals("456", user.getName());
    }
    
    @Test
    @InSequence(4)
    public void testRemove() {
        UserEntity user = userService.getUser(userId);
        userService.removeUser(user);
        
        user = userService.getUser(user.getId());
        Assert.assertNull(user);
    }
}
