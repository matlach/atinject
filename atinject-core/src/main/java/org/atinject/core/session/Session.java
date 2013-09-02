package org.atinject.core.session;


public interface Session {
    
    String getSessionId();

    Session setSessionId(String sessionId);
    
    String getMachineId();

    Session setMachineId(String machineId);
    
}