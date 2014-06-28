package org.atinject.api.userrelocation.event;

import java.util.UUID;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.event.Event;

public class UserRelocated extends Event {

    private static final long serialVersionUID = 1L;

    private UserEntity user;

    private UUID oldUserId;

    public UUID getOldUserId() {
        return oldUserId;
    }

    public UserRelocated setOldUserId(UUID oldUserId) {
        this.oldUserId = oldUserId;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public UserRelocated setUser(UserEntity user) {
        this.user = user;
        return this;
    }
    
}
