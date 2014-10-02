package org.atinject.core.rendezvous.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.atinject.core.entity.Entity;

public class RendezvousEntity extends Entity {

    private static final long serialVersionUID = 1L;

    private UUID id;
    
    private Set<String> sessionIds;

    public RendezvousEntity(){
        sessionIds = new HashSet<>();
    }
    
    @Override
	public UUID getId() {
        return id;
    }

    public RendezvousEntity setId(UUID id) {
        this.id = id;
        return this;
    }
    
    public Set<String> getSessionIds(){
        return sessionIds;
    }
    
}
