package org.atinject.api.user;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DefaultUserIdGenerator implements UserIdGenerator {

	@Override
	public UUID generateUserId() {
		// TODO replace random uuid with version 1 uuid
		// cassandra uses(?) http://johannburkard.de/software/uuid/#maven
		return UUID.randomUUID();
	}

}
