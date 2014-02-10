package org.atinject.api.user;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.affinity.AffineVersion4UUIDGenerator;

@ApplicationScoped
public class DefaultUserIdGenerator implements UserIdGenerator {

    @Inject
    private AffineVersion4UUIDGenerator affineVersion4UUIDGenerator;
    
    @Override
    public UUID generateUserId() {
        return affineVersion4UUIDGenerator.getLocalKey();
    }

    @Override
    public UUID generateUserId(String machineId) {
        return affineVersion4UUIDGenerator.getRemoteKey(machineId);
    }
}
