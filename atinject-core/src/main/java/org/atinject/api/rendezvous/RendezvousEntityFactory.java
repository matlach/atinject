package org.atinject.api.rendezvous;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.api.rendezvous.entity.RendezvousEntity;
import org.atinject.core.entity.EntityFactory;

@ApplicationScoped
public class RendezvousEntityFactory extends EntityFactory {

    public RendezvousEntity newRendezvous(){
        return new RendezvousEntity();
    }
}
