package org.atinject.core.notification;

import org.atinject.api.session.Session;
import org.atinject.core.websocket.dto.BaseWebSocketNotification;

public class NotificationEvent
{
    private Session session;
    private BaseWebSocketNotification notification;
    
    public Session getSession()
    {
        return session;
    }
    public void setSession(Session session)
    {
        this.session = session;
    }
    public BaseWebSocketNotification getNotification()
    {
        return notification;
    }
    public void setNotification(BaseWebSocketNotification notification)
    {
        this.notification = notification;
    }
    
    
}
