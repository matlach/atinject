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
    public NotificationEvent setSession(Session session)
    {
        this.session = session;
        return this;
    }
    public BaseWebSocketNotification getNotification()
    {
        return notification;
    }
    public NotificationEvent setNotification(BaseWebSocketNotification notification)
    {
        this.notification = notification;
        return this;
    }
    
    
}
