package org.atinject.api.session.dto;

import org.atinject.core.websocket.dto.WebSocketNotification;

public class SessionOpenedNotification extends WebSocketNotification
{
    private static final long serialVersionUID = 1L;
    
    public String sessionId;
    
    public SessionOpenedNotification(){
        super();
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public SessionOpenedNotification setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
        return this;
    }
    
}
