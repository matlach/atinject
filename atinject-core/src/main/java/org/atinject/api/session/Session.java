package org.atinject.api.session;

import org.atinject.core.marshallable.MarshallableObject;

public class Session extends MarshallableObject {
    
    private static final long serialVersionUID = 1L;

    private String sessionId;
    
    private Integer channelId;
    
    private String machineId;
    
    private String userId;
    
    public Session(){
        super();
    }

    public String getSessionId() {
        return sessionId;
    }

    public Session setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }
    
    public Integer getChannelId() {
        return channelId;
    }

    public Session setChannelId(Integer channelId) {
        this.channelId = channelId;
        return this;
    }

    public String getMachineId() {
        return machineId;
    }

    public Session setMachineId(String machineId) {
        this.machineId = machineId;
        return this;
    }
    
    public String getUserId() {
        return userId;
    }

    public Session setUserId(String userId) {
        this.userId = userId;
        return this;
    }
}