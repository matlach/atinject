package org.atinject.api.user;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DefaultUserIdGenerator implements UserIdGenerator {

	@Override
	public String generateUserId() {
		return UUID.randomUUID().toString();
	}

}
