package org.atinject.api.usersession;

import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

import javax.inject.Inject;

import org.atinject.core.session.Session;
import org.atinject.integration.ArquillianIT;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

public class UserSessionServiceIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
        
        File logbackXmlFile = Paths.get("src/test/resources/default").resolve("logback.xml").toFile().getAbsoluteFile();
        File validationXmlFile = Paths.get("src/test/resources/default").resolve("validation.xml").toFile();
        File beansXmlFile = Paths.get("src/test/resources/default").resolve("beans.xml").toFile();
        File javaxEnterpriseInjectSpiExtensionFile = Paths.get("src/main/resources/default").resolve("javax.enterprise.inject.spi.Extension").toFile();
        
        JavaArchive archive = createArchive(ArquillianIT.class);
        addPackageAndItIsDependencies(archive, api_user_session);
        
        archive
                //.addAsManifestResource(beansXmlFile, "beans.xml")
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource(validationXmlFile, "validation.xml")
                .addAsManifestResource(javaxEnterpriseInjectSpiExtensionFile, "services/javax.enterprise.inject.spi.Extension")
                .addAsResource(logbackXmlFile, "/logback.xml");
        
        return archive;
    }
    
    @Inject
    private Logger logger;
    
    @Inject
    private UserSessionService sessionService;
    
    @Inject
    private UserSessionFactory sessionFactory;
    
    @Test
    public void testOpenSession()
    {
        Session newSession = sessionFactory.newSession()
                .setSessionId("123");
        
        sessionService.openSession(newSession);
        
        Session session = sessionService.getSession(newSession.getSessionId());
        
        Assert.assertEquals(newSession, session);
    }
    
    @Test
    public void testCloseSession(){
        Session newSession = sessionFactory.newSession()
                .setSessionId("123");
        
        sessionService.openSession(newSession);
        
        sessionService.closeSession(newSession);
        
        Session session = sessionService.getSession(newSession.getSessionId());
        
        Assert.assertNull(session);
    }
    
    @Test
    public void testGetSessionByUserId(){
        UUID userId = UUID.randomUUID();
        UserSession newSession = sessionFactory.newSession()
                .setSessionId("123")
                .setUserId(userId);
        
        sessionService.openSession(newSession);
        
        Session session = sessionService.getSessionByUserId(userId);
        
        Assert.assertEquals(newSession, session);
    }
    
    @Test
    public void testUpdateSession(){
        UUID userId = UUID.randomUUID();
        UserSession newSession = sessionFactory.newSession()
                .setSessionId("123")
                .setUserId(userId);
        
        sessionService.openSession(newSession);
        
        userId = UUID.randomUUID();
        UserSession session = sessionService.getSession("123");
        session.setUserId(userId);
        
        session = sessionService.getSession("123");
        
        Assert.assertEquals(userId, session.getUserId());
    }
}
