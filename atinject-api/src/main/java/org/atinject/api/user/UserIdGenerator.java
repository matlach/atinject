package org.atinject.api.user;

import java.util.UUID;

public interface UserIdGenerator {

    UUID generateUserId();
    
    UUID generateUserId(String machineId);
}
