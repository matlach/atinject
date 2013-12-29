package org.atinject.integration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.atinject.api.user.UserServiceIT;
import org.atinject.api.usersession.UserSessionFactory;
import org.atinject.api.usersession.UserSessionService;
import org.atinject.core.concurrent.AsynchronousInterceptor;
import org.atinject.core.concurrent.RetryInterceptor;
import org.atinject.core.profiling.ProfileInterceptor;
import org.atinject.core.thread.ThreadTrackerInterceptor;
import org.atinject.core.tiers.exception.HandleCacheExceptionInterceptor;
import org.atinject.core.tiers.exception.HandleServiceExceptionInterceptor;
import org.atinject.core.tiers.exception.HandleWebSocketServiceExceptionInterceptor;
import org.atinject.core.transaction.TransactionalInterceptor;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.junit.Test;

//@Ignore
public class ManualTesting extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        
        File logbackXmlFile = Paths.get("src/test/resources/default").resolve("logback.xml").toFile().getAbsoluteFile();
        File validationXmlFile = Paths.get("src/test/resources/default").resolve("validation.xml").toFile();
        File javaxEnterpriseInjectSpiExtensionFile = Paths.get("src/main/resources/default").resolve("javax.enterprise.inject.spi.Extension").toFile();

        BeansDescriptor beans = Descriptors.create(BeansDescriptor.class)
                .createAlternatives()
                    .clazz(UserSessionFactory.class.getCanonicalName())
                    .clazz(UserSessionService.class.getCanonicalName())
                    .up()
                .createDecorators()
                    .up()
                .createInterceptors()
                    .clazz(AsynchronousInterceptor.class.getCanonicalName())
                    .clazz(RetryInterceptor.class.getCanonicalName())
                    .clazz(ProfileInterceptor.class.getCanonicalName())
                    .clazz(ThreadTrackerInterceptor.class.getCanonicalName())
                    .clazz(HandleWebSocketServiceExceptionInterceptor.class.getCanonicalName())
                    .clazz(HandleServiceExceptionInterceptor.class.getCanonicalName())
                    .clazz(HandleCacheExceptionInterceptor.class.getCanonicalName())
                    .clazz(TransactionalInterceptor.class.getCanonicalName())
                    .up();
        
        JavaArchive archive = createArchive(UserServiceIT.class);
        addPackageAndItIsDependencies(archive, framework);
        
        archive
//                .addAsManifestResource(beansXmlFile, "beans.xml")
                .addAsManifestResource(new StringAsset(beans.exportAsString()), "beans.xml")
                .addAsManifestResource(validationXmlFile, "validation.xml")
                .addAsManifestResource(javaxEnterpriseInjectSpiExtensionFile, "services/javax.enterprise.inject.spi.Extension")
                .addAsResource(logbackXmlFile, "/logback.xml");
        
        return archive;
    }
    
    @Test
    public void testManually() throws IOException {
        System.in.read();
    }
}
