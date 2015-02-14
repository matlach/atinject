package org.atinject.api.usersession;

import java.util.Optional;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import org.atinject.core.cache.ReplicatedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.session.SessionService;

@Alternative @Specializes
@ApplicationScoped
public class UserSessionService extends SessionService {

    @Inject @Named("session") private ReplicatedCache<UUID, UserSession> cache;
    
    public UserSession getSessionByUserId(UUID userId) {
    	return cache.values()
    			.parallel()
    			.filter((session) -> userId.equals(session.getUserId()))
    					.findFirst()
    					.orElse(null);
    }
    
    @Override
    public Optional<UserSession> getSession(String sessionId) {
        return (Optional<UserSession>) super.getSession(sessionId);
    }
    
}
