package org.atinject.api.rendezvous;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.atinject.api.rendezvous.event.SessionJoinedRendezvous;
import org.atinject.api.rendezvous.event.SessionLeftRendezvous;
import org.atinject.core.tiers.WebSocketService;

@ApplicationScoped
public class RendezvousWebSocketService extends WebSocketService {

    public void onSessionJoinedRendezvous(@Observes SessionJoinedRendezvous event) {
        // send notification to others
    }
    
    public void onSessionLeftRendezvous(@Observes SessionLeftRendezvous event) {
        // send notification to others
    }
}
