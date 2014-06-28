package org.atinject.api.usercredential;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.uuid.Version4UUIDGenerator;

@ApplicationScoped
public class DefaultdSaltGenerator implements SaltGenerator {

    @Inject
    private Version4UUIDGenerator generator;
    
    @Override
    public String get() {
        return generator.get().toString();
    }
}
