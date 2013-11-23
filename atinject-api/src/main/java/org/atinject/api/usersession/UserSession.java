package org.atinject.api.usersession;

import java.util.UUID;

import org.atinject.core.session.Session;
import org.atinject.core.session.SimpleSession;

public class UserSession extends SimpleSession implements Session {

    private static final long serialVersionUID = 1L;

    private UUID userId;
    
    @Override
    public UserSession setSessionId(String sessionId) {
    	super.setSessionId(sessionId);
    	return this;
    }
    
    @Override
    public UserSession setMachineId(String machineId) {
    	super.setMachineId(machineId);
    	return this;
    }
    
    public UUID getUserId() {
        return userId;
    }

    public UserSession setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }
    
}
