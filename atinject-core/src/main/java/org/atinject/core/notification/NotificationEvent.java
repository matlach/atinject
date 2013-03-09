package org.atinject.core.notification;

import org.atinject.api.session.Session;
import org.atinject.core.websocket.dto.WebSocketNotification;

public class NotificationEvent
{
    private Session session;
    private WebSocketNotification notification;
    
    public Session getSession()
    {
        return session;
    }
    public NotificationEvent setSession(Session session)
    {
        this.session = session;
        return this;
    }
    public WebSocketNotification getNotification()
    {
        return notification;
    }
    public NotificationEvent setNotification(WebSocketNotification notification)
    {
        this.notification = notification;
        return this;
    }
    
    
}
