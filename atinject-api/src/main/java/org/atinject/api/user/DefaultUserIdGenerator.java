package org.atinject.api.user;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.uuid.Version4UUIDGenerator;

@ApplicationScoped
public class DefaultUserIdGenerator implements UserIdGenerator {

    // TODO version1 instead ?
    @Inject
    private Version4UUIDGenerator uuidGenerator;
    
    @Override
    public UUID generateUserId() {
        return uuidGenerator.get();
    }

}
