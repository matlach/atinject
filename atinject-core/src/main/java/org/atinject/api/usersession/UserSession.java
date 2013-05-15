package org.atinject.api.usersession;

import org.atinject.core.marshallable.MarshallableObject;
import org.atinject.core.session.Session;

public class UserSession extends MarshallableObject implements Session {

    private static final long serialVersionUID = 1L;

    private String sessionId;
    
    private Integer channelId;
    
    private String machineId;
    
    private String userId;
    
    public UserSession() {
        super();
    }
    
    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public UserSession setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }
    
    @Override
    public Integer getChannelId() {
        return channelId;
    }

    @Override
    public UserSession setChannelId(Integer channelId) {
        this.channelId = channelId;
        return this;
    }

    @Override
    public String getMachineId() {
        return machineId;
    }

    @Override
    public UserSession setMachineId(String machineId) {
        this.machineId = machineId;
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
