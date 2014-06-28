package org.atinject.api.usersession;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.session.SessionContext;

@ApplicationScoped
public class UserSessionContext {

	@Inject
	private SessionContext sessionContext;
	
    public UserSession get() {
        return (UserSession) sessionContext.get();
    }
    
    public void set(UserSession session) {
        sessionContext.set(session);
    }
    
    public void remove() {
        sessionContext.remove();
    }
}
