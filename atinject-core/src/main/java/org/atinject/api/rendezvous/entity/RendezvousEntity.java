package org.atinject.api.rendezvous.entity;

import java.util.HashSet;
import java.util.Set;

import org.atinject.core.entity.Entity;

public class RendezvousEntity extends Entity {

    private static final long serialVersionUID = 1L;

    private String id;
    
    private Set<String> sessionIds;
    
    public String getId() {
        return id;
    }

    public RendezvousEntity setId(String id) {
        this.id = id;
        return this;
    }
    
    public RendezvousEntity(){
        sessionIds = new HashSet<>();
    }
    
    public Set<String> getSessionIds(){
        return sessionIds;
    }
    
}
