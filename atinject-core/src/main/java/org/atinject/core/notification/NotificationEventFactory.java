package org.atinject.core.notification;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.core.event.EventFactory;

@ApplicationScoped
public class NotificationEventFactory extends EventFactory {

    
    public NotificationEvent newNotificationEvent() {
        return new NotificationEvent();
    }
}
