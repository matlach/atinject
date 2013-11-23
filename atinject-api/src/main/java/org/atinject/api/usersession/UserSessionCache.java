package org.atinject.api.usersession;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import org.atinject.core.cache.ReplicatedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.session.SessionCache;

@Alternative @Specializes
@ApplicationScoped
public class UserSessionCache extends SessionCache {

    @Inject @Named("session") private ReplicatedCache<UUID, UserSession> cache;
    
    public UserSession getSessionByUserId(UUID userId){
        // TODO this is a good candidate for parallel iteration
        for (UserSession session : cache.values()){
            if (session.getUserId().equals(userId)){
                return session;
            }
        }
        return null;
    }
    
}
