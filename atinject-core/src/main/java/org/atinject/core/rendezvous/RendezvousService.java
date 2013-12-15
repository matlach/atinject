package org.atinject.core.rendezvous;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.core.affinity.LocalRandomUUIDGenerator;
import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.rendezvous.entity.RendezvousEntity;
import org.atinject.core.rendezvous.event.SessionJoinedRendezvous;
import org.atinject.core.rendezvous.event.SessionLeftRendezvous;
import org.atinject.core.session.Session;
import org.atinject.core.session.event.SessionClosed;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class RendezvousService extends Service {

    @Inject
    RendezvousEntityFactory entityFactory;

    @Inject @Named("rendezvous") private DistributedCache<UUID, RendezvousEntity> rendezvousCache;

    @Inject private LocalRandomUUIDGenerator generator;
    
    @Inject
    Event<SessionJoinedRendezvous> sessionJoinedRendezvousEvent;
    @Inject
    Event<SessionLeftRendezvous> sessionLeftRendezvousEvent;

    public RendezvousEntity newRendezvous() {
        UUID rendezvousId = generator.getKey();
        RendezvousEntity rendezvous = entityFactory.newRendezvous()
                .setId(rendezvousId);
        return rendezvous;
    }

    public void onSessionClosed(@Observes SessionClosed event) {
        List<UUID> rendezvousIds = new ArrayList<>();
        for (RendezvousEntity rendezvous : rendezvousCache.getAll().values()) {
            if (rendezvous.getSessionIds().contains(event.getSession().getSessionId())) {
                rendezvousIds.add(rendezvous.getId());
            }
        }
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
        RendezvousEntity rendezvous = rendezvousCache.get(rendezvousId);
        if (rendezvous == null) {
            throw new NullPointerException("rendezvous does not exists");
        }
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
        RendezvousEntity rendezvous = rendezvousCache.get(rendezvousId);
        if (rendezvous == null) {
            throw new NullPointerException("rendezvous does not exists");
        }
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
}
