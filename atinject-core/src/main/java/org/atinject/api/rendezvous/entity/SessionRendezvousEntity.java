package org.atinject.api.rendezvous.entity;

import java.util.Set;

import org.atinject.core.entity.Entity;

public class SessionRendezvousEntity extends Entity {

    private static final long serialVersionUID = 1L;
    
    private String sessionId;
    private Set<String> rendezvousIds;
}
