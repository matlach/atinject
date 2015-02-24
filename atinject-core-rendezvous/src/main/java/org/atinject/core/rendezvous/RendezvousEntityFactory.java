package org.atinject.core.rendezvous;

import javax.enterprise.context.ApplicationScoped;

import org.atinject.core.entity.EntityFactory;
import org.atinject.core.rendezvous.entity.RendezvousEntity;

@ApplicationScoped
public class RendezvousEntityFactory extends EntityFactory {

    public RendezvousEntity newRendezvous(){
        return new RendezvousEntity();
    }
}
