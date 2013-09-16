package org.atinject.core.session;

import org.atinject.core.marshallable.MarshallableObject;

public class SimpleSession extends MarshallableObject implements Session {

    private static final long serialVersionUID = 1L;

    private String sessionId;
    
    private String machineId;
    
    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public SimpleSession setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }
    
    @Override
    public String getMachineId() {
        return machineId;
    }

    @Override
    public SimpleSession setMachineId(String machineId) {
        this.machineId = machineId;
        return this;
    }
    
}
