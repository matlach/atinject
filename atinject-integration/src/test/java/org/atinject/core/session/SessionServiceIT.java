package org.atinject.core.session;

import javax.inject.Inject;

import org.atinject.api.usersession.UserSession;
import org.atinject.api.usersession.UserSessionFactory;
import org.atinject.api.usersession.UserSessionService;
import org.atinject.integration.IntegrationTest;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SessionServiceIT extends IntegrationTest
{

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
        UserSession newSession = sessionFactory.newSession()
                .setSessionId("123")
                .setUserId("456");
        
        sessionService.openSession(newSession);
        
        Session session = sessionService.getSessionByUserId("456");
        
        Assert.assertEquals(newSession, session);
    }
    
    @Test
    public void testUpdateSession(){
        UserSession newSession = sessionFactory.newSession()
                .setSessionId("123")
                .setUserId("456");
        
        sessionService.openSession(newSession);
        
        UserSession session = sessionService.getSession("123");
        session.setUserId("789");
        
        session = sessionService.getSession("123");
        
        Assert.assertEquals("789", session.getUserId());
    }
}
