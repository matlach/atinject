package org.atinject.api.usersession;

import java.util.UUID;

import javax.inject.Inject;

import org.assertj.core.api.Assertions;
import org.atinject.core.cache.CacheExtension;
import org.atinject.core.session.Session;
import org.atinject.core.transaction.InMemoryTransactionServices;
import org.atinject.integration.ArquillianIT;
import org.atinject.integration.DefaultDeployment;
import org.atinject.integration.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.slf4j.Logger;

public class UserSessionServiceIT extends IntegrationTest {

    @Deployment
    public static JavaArchive createDeployment() {
    	return new DefaultDeployment(ArquillianIT.class)
			.appendEmptyBeansXml()
			.appendJavaxEnterpriseInjectSpiExtension(CacheExtension.class)
			.appendOrgJBossWeldBootstrapApiService(InMemoryTransactionServices.class)
			.appendResource("arquillian-logback.xml", "logback.xml")
			.appendResource("arquillian-jgroups.xml", "jgroups.xml")
			.getArchive();
    }
    
    @Inject
    private Logger logger;
    
    @Inject
    private UserSessionService sessionService;
    
    @Inject
    private UserSessionFactory sessionFactory;
    
    @Test @InSequence(1)
    public void testOpenSession() {
        Session newSession = sessionFactory.newSession()
                .setSessionId("123");
        
        sessionService.openSession(newSession);
        
        Session session = sessionService.getSession(newSession.getSessionId()).get();
        
        Assertions.assertThat(newSession).isEqualTo(session);
    }
    
    @Test @InSequence(2)
    public void testCloseSession() {
        Session newSession = sessionFactory.newSession()
                .setSessionId("123");
        
        sessionService.openSession(newSession);
        
        sessionService.closeSession(newSession);
        
        Session session = sessionService.getSession(newSession.getSessionId()).orElse(null);
        
        Assertions.assertThat(session).isNull();
    }
    
    @Test @InSequence(3)
    public void testGetSessionByUserId() {
        UUID userId = UUID.randomUUID();
        UserSession newSession = sessionFactory.newSession()
                .setSessionId("123")
                .setUserId(userId);
        
        sessionService.openSession(newSession);
        
        Session session = sessionService.getSessionByUserId(userId);
        
        Assertions.assertThat(newSession).isEqualTo(session);
    }
    
    @Test @InSequence(4)
    public void testUpdateSession() {
        UUID userId = UUID.randomUUID();
        UserSession newSession = sessionFactory.newSession()
                .setSessionId("123")
                .setUserId(userId);
        
        sessionService.openSession(newSession);
        
        userId = UUID.randomUUID();
        UserSession session = sessionService.getSession("123").get();
        session.setUserId(userId);
        
        session = sessionService.getSession("123").get();
        
        Assertions.assertThat(userId).isEqualTo(session.getUserId());
    }
}
