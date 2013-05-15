package org.atinject.api.usersession;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import org.atinject.core.session.SessionService;

@Alternative @Specializes
@ApplicationScoped
public class UserSessionService extends SessionService {

    @Inject UserSessionCache userSessionCache;
    
    public UserSession getSessionByUserId(String userId){
        return userSessionCache.getSessionByUserId(userId);
    }    
    
    @Override
    public UserSession getSession(String sessionId){
        return (UserSession) super.getSession(sessionId);
    }
    
}
