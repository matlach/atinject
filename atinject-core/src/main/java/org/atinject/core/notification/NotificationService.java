package org.atinject.core.notification;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.cdi.CDI;
import org.atinject.core.rendezvous.entity.RendezvousEntity;
import org.atinject.core.session.Session;
import org.atinject.core.session.SessionService;
import org.atinject.core.topology.TopologyDistributedExecutor;
import org.atinject.core.websocket.dto.WebSocketNotification;

@ApplicationScoped
public class NotificationService {
    
    @Inject NotificationEventFactory notificationEventFactory;
    
    @Inject TopologyDistributedExecutor distributedExecutor;
    
    @Inject SessionService sessionService;
    
    public Future<Void> sendNotification(Session session, WebSocketNotification notification){
        
        NotificationEvent event = notificationEventFactory.newNotificationEvent()
                .setSession(session)
                .setNotification(notification);
        
        SendNotificationTask task = new SendNotificationTask();
        task.setEvent(event);
        
        return distributedExecutor.submit(session.getMachineId(), task);
    }

    // TODO implements batching
    public Future<Void> sendNotification(RendezvousEntity rendezvous, WebSocketNotification notification) {
        for (String sessionId : rendezvous.getSessionIds()) {
            Session session = sessionService.getSession(sessionId);
            if (session != null) {
                sendNotification(session, notification);
            }
        }
        return null;
    }
    
    public static class SendNotificationTask implements Callable<Void> {

        private NotificationEvent event;
        
        public NotificationEvent getEvent()
        {
            return event;
        }

        public void setEvent(NotificationEvent event)
        {
            this.event = event;
        }

        @Override
        public Void call() throws Exception
        {
            CDI.getBeanManager().fireEvent(event);
            return null;
        }
        
    }
    
}
