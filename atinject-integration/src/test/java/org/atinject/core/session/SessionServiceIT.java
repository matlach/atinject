package org.atinject.core.session;

import javax.inject.Inject;

import org.atinject.api.session.Session;
import org.atinject.api.session.SessionService;
import org.atinject.api.session.event.SessionClosed;
import org.atinject.api.session.event.SessionOpened;
import org.atinject.integration.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;

public class SessionServiceIT extends IntegrationTest
{

    @Inject
    private Logger logger;
    
    @Inject
    private SessionService sessionService;
    
    @Test
    public void testSessionOpened()
    {
        Session newSession = new Session();
        newSession.setSessionId("123");
        
        SessionOpened sessionOpened = new SessionOpened();
        sessionOpened.setSession(newSession);
        sessionService.onSessionOpened(sessionOpened);
        
        Session session = sessionService.getSession(newSession.getSessionId());
        
        Assert.assertEquals(newSession, session);
        
        // TODO create SessionService @Alternative for unit test and provide clear method
    }
    
    @Test
    public void testSessionClosed(){
        Session newSession = new Session();
        newSession.setSessionId("123");
        
        SessionOpened sessionOpened = new SessionOpened();
        sessionOpened.setSession(newSession);
        sessionService.onSessionOpened(sessionOpened);
        
        SessionClosed sessionClosed = new SessionClosed();
        sessionClosed.setSession(newSession);
        sessionService.onSessionClosed(sessionClosed);
        
        Session session = sessionService.getSession(newSession.getSessionId());
        
        Assert.assertNull(session);
    }
    
    @Test
    public void testGetSessionByUserId(){
        Session newSession = new Session();
        newSession.setSessionId("123");
        newSession.setUserId("456");
        
        SessionOpened sessionOpened = new SessionOpened();
        sessionOpened.setSession(newSession);
        sessionService.onSessionOpened(sessionOpened);
        
        Session session = sessionService.getSessionByUserId("456");
        
        Assert.assertEquals(newSession, session);
    }
    
    @Test
    public void testUpdateSession(){
        Session newSession = new Session();
        newSession.setSessionId("123");
        newSession.setUserId("456");
        
        SessionOpened sessionOpened = new SessionOpened();
        sessionOpened.setSession(newSession);
        sessionService.onSessionOpened(sessionOpened);
        
        Session session = sessionService.getSession("123");
        session.setUserId("789");
        
        session = sessionService.getSession("123");
        
        Assert.assertEquals("789", session.getUserId());
    }
}
