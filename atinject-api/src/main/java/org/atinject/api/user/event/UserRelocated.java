package org.atinject.api.user.event;

import java.util.UUID;

import org.atinject.core.event.Event;

public class UserRelocated extends Event {

    private static final long serialVersionUID = 1L;

    private UUID oldUserId;
    
    private UUID newUserId;

    public UUID getOldUserId() {
        return oldUserId;
    }

    public UserRelocated setOldUserId(UUID oldUserId) {
        this.oldUserId = oldUserId;
        return this;
    }

    public UUID getNewUserId() {
        return newUserId;
    }

    public UserRelocated setNewUserId(UUID newUserId) {
        this.newUserId = newUserId;
        return this;
    }
    
}
