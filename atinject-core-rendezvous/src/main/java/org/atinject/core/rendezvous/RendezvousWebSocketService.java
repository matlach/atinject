package org.atinject.core.rendezvous;

import javax.enterprise.event.Observes;

import org.atinject.core.rendezvous.event.SessionJoinedRendezvous;
import org.atinject.core.rendezvous.event.SessionLeftRendezvous;
import org.atinject.core.tiers.WebSocketService;

@WebSocketService
public class RendezvousWebSocketService {

    public void onSessionJoinedRendezvous(@Observes SessionJoinedRendezvous event) {
        // send notification to others
    }
    
    public void onSessionLeftRendezvous(@Observes SessionLeftRendezvous event) {
        // send notification to others
    }
}
