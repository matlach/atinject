package org.atinject.core.rendezvous;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.core.affinity.AffineVersion4UUIDGenerator;
import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.notification.NotificationService;
import org.atinject.core.rendezvous.entity.RendezvousEntity;
import org.atinject.core.rendezvous.event.SessionJoinedRendezvous;
import org.atinject.core.rendezvous.event.SessionLeftRendezvous;
import org.atinject.core.session.Session;
import org.atinject.core.session.SessionService;
import org.atinject.core.session.event.SessionClosed;
import org.atinject.core.tiers.Service;
import org.atinject.core.websocket.dto.WebSocketNotification;

@Service
public class RendezvousService {

    @Inject
    RendezvousEntityFactory entityFactory;

    @Inject @Named("rendezvous") private DistributedCache<UUID, RendezvousEntity> rendezvousCache;

    @Inject private AffineVersion4UUIDGenerator generator;
    
    @Inject
    Event<SessionJoinedRendezvous> sessionJoinedRendezvousEvent;
    
    @Inject
    Event<SessionLeftRendezvous> sessionLeftRendezvousEvent;
    
    @Inject NotificationService notificationService;
    @Inject SessionService sessionService;

    public RendezvousEntity newRendezvous() {
        UUID rendezvousId = generator.getLocalKey();
        RendezvousEntity rendezvous = entityFactory.newRendezvous()
                .setId(rendezvousId);
        return rendezvous;
    }

    public void onSessionClosed(@Observes SessionClosed event) {
        List<UUID> rendezvousIds = rendezvousCache.values()
        	.filter(rendezvous -> rendezvous.getSessionIds().contains(event.getSession().getSessionId()))
        	.map(rendezvous -> rendezvous.getId())
        	.collect(Collectors.toList());
        for (UUID rendezvousId : rendezvousIds) {
            leave(rendezvousId, event.getSession());
        }
    }

    public void addRendezvous(RendezvousEntity rendezvous) {
        rendezvousCache.put(rendezvous.getId(), rendezvous);
    }

    public RendezvousEntity addAndJoin(Session session) {
        RendezvousEntity rendezvous = newRendezvous();
        rendezvousCache.lock(rendezvous.getId());
        rendezvous.getSessionIds().add(session.getSessionId());
        addRendezvous(rendezvous);
        return rendezvous;
    }

    public RendezvousEntity join(UUID rendezvousId, Session session) {
        rendezvousCache.lock(rendezvousId);
        RendezvousEntity rendezvous = rendezvousCache.get(rendezvousId)
        		.orElseThrow(() -> new NullPointerException("rendezvous does not exists"));
        return join(rendezvous, session);
    }

    public RendezvousEntity join(RendezvousEntity rendezvous, Session session) {
        if (rendezvous.getSessionIds().contains(session.getSessionId())) {
            throw new RuntimeException("already joined");
        }
        rendezvous.getSessionIds().add(session.getSessionId());
        sessionJoinedRendezvousEvent.fire(null);
        return rendezvous;
    }

    public RendezvousEntity leave(UUID rendezvousId, Session session) {
        rendezvousCache.lock(rendezvousId);
        RendezvousEntity rendezvous = rendezvousCache.get(rendezvousId)
        		.orElseThrow(() -> new NullPointerException("rendezvous does not exists"));
        return leave(rendezvous, session);
    }

    public RendezvousEntity leave(RendezvousEntity rendezvous, Session session) {
        if (!rendezvous.getSessionIds().contains(session.getSessionId())) {
            throw new RuntimeException("already left");
        }
        rendezvous.getSessionIds().remove(session.getSessionId());
        sessionLeftRendezvousEvent.fire(null);
        return rendezvous;
    }
    
    // TODO implements batching
    public Future<Void> sendNotification(RendezvousEntity rendezvous, WebSocketNotification notification) {
        for (String sessionId : rendezvous.getSessionIds()) {
            Session session = sessionService.getSession(sessionId).orElse(null);
            if (session != null) {
                notificationService.sendNotification(session, notification);
            }
        }
        return null;
    }
}
