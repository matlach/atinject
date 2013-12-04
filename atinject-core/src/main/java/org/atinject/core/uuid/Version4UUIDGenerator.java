package org.atinject.core.uuid;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Version4UUIDGenerator extends AbstractUUIDGenerator {

    @Override
    public UUID get() {
        return UUID.randomUUID();
    }

}
