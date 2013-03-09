package org.atinject.core.notification;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.session.Session;
import org.atinject.core.cdi.BeanManagerExtension;
import org.atinject.core.distexec.TopologyDistributedExecutor;
import org.atinject.core.websocket.dto.WebSocketNotification;

@ApplicationScoped
public class NotificationService
{
    
    @Inject
    private TopologyDistributedExecutor distributedExecutor;
    
    public Future<Void> sendNotification(Session session, WebSocketNotification notification){
        
        NotificationEvent event = new NotificationEvent();
        event.setSession(session);
        event.setNotification(notification);
        
        SendNotificationTask task = new SendNotificationTask();
        task.setEvent(event);
        
        return distributedExecutor.submit(session.getMachineId(), session.getRackId(), session.getSiteId(), task);
    }
    
    public class SendNotificationTask implements Callable<Void>{

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
            BeanManagerExtension.fireEvent(event);
            return null;
        }
        
    }
    
}
