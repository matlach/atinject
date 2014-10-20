package org.atinject.api.useraffinity;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.authentication.event.UserLoggedIn;
import org.atinject.api.useraffinity.dto.UserAffinityNotification;
import org.atinject.api.usertopology.UserTopologyService;
import org.atinject.core.notification.NotificationService;
import org.atinject.core.tiers.WebSocketService;
import org.infinispan.remoting.transport.TopologyAwareAddress;

@WebSocketService
public class UserAffinityWebSocketService {

    @Inject NotificationService notificationService;
    
    @Inject UserTopologyService userTopologyService;
    
    public void onUserLoggedIn(@Observes UserLoggedIn event){
        // user logged in locally i.e. on this websocket
        if (event.isOriginLocal()){
            // get user affinity
            TopologyAwareAddress topologyAwareAddress = userTopologyService.getUserKeyPrimaryLocation(event.getUser().getId());
            
            // send notification with user affinity information
            UserAffinityNotification userAffinityNotification = new UserAffinityNotification()
                .setUrl(topologyAwareAddress.getMachineId());
            notificationService.sendNotification(event.getSession(), userAffinityNotification);
        }
    }
    
    // TODO on topology change (begin), send potential server slow down warning notification
    // TODO on topology changed (completed), send potential server slow down back to normal
    // TODO on topology changed (completed), recompute for each local client (i.e. session with machineId = localAddress.machineId) the best affinity
}
