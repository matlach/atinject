package org.atinject.core.session;


public interface Session {
    
    String getSessionId();

    Session setSessionId(String sessionId);
    
    Integer getChannelId();

    Session setChannelId(Integer channelId);

    String getMachineId();

    Session setMachineId(String machineId);
    
}