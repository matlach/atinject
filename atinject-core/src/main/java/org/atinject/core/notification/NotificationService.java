package org.atinject.core.notification;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.rendezvous.entity.RendezvousEntity;
import org.atinject.api.session.Session;
import org.atinject.api.session.SessionService;
import org.atinject.core.cdi.CDI;
import org.atinject.core.topology.TopologyDistributedExecutor;
import org.atinject.core.websocket.dto.WebSocketNotification;

@ApplicationScoped
public class NotificationService
{
    
    @Inject private TopologyDistributedExecutor distributedExecutor;
    
    @Inject private SessionService sessionService;
    
    public Future<Void> sendNotification(Session session, WebSocketNotification notification){
        
        NotificationEvent event = new NotificationEvent();
        event.setSession(session);
        event.setNotification(notification);
        
        SendNotificationTask task = new SendNotificationTask();
        task.setEvent(event);
        
        return distributedExecutor.submit(session.getMachineId(), task);
    }

    // TODO implements batching
    public Future<Void> sendNotification(RendezvousEntity rendezvous, WebSocketNotification notification){
        for(String sessionId : rendezvous.getSessionIds()){
            Session session = sessionService.getSession(sessionId);
            sendNotification(session, notification);
        }
        return null;
    }
    
    public static class SendNotificationTask implements Callable<Void>{

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
