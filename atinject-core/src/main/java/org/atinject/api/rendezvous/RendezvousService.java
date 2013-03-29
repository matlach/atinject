package org.atinject.api.rendezvous;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.rendezvous.entity.RendezvousEntity;
import org.atinject.api.session.Session;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class RendezvousService extends Service {

    @Inject private RendezvousCache rendezvousCache;
    
    public RendezvousEntity newRendezvous(){
        String rendezvousId = rendezvousCache.getId();
        RendezvousEntity rendezvous = new RendezvousEntity()
            .setId(rendezvousId);
        return rendezvous;
    }
    
    public void addRendezvous(RendezvousEntity rendezvous){
        rendezvousCache.putRendezvous(rendezvous);
    }
    
    public RendezvousEntity join(String rendezvousId, Session session){
        rendezvousCache.lockRendezvous(rendezvousId);
        RendezvousEntity rendezvous = rendezvousCache.getRendezvous(rendezvousId);
        if (rendezvous == null){
            throw new NullPointerException("rendezvous does not exists");
        }
        return join(rendezvous, session);
    }
    
    public RendezvousEntity join(RendezvousEntity rendezvous, Session session){
        if (rendezvous.getSessionIds().contains(session.getSessionId())){
            throw new RuntimeException("already joined");
        }
        rendezvous.getSessionIds().add(session.getSessionId());
        return rendezvous;
    }
    
    public RendezvousEntity leave(String rendezvousId, Session session){
        rendezvousCache.lockRendezvous(rendezvousId);
        RendezvousEntity rendezvous = rendezvousCache.getRendezvous(rendezvousId);
        if (rendezvous == null){
            throw new NullPointerException("rendezvous does not exists");
        }
        return leave(rendezvous, session);
    }
    
    public RendezvousEntity leave(RendezvousEntity rendezvous, Session session){
        if (! rendezvous.getSessionIds().contains(session.getSessionId())){
            throw new RuntimeException("already left");
        }
        rendezvous.getSessionIds().remove(session.getSessionId());
        return rendezvous;
    }
}
