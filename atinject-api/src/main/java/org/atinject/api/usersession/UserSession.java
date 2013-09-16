package org.atinject.api.usersession;

import org.atinject.core.session.Session;
import org.atinject.core.session.SimpleSession;

public class UserSession extends SimpleSession implements Session {

    private static final long serialVersionUID = 1L;

    private String userId;
    
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
    
    public String getUserId() {
        return userId;
    }

    public UserSession setUserId(String userId) {
        this.userId = userId;
        return this;
    }
    
}
