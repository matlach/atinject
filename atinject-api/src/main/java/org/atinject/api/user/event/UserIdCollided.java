package org.atinject.api.user.event;

import java.util.UUID;

public class UserIdCollided {

	private UUID userId;

	public UUID getUserId() {
		return userId;
	}

	public UserIdCollided setUserId(UUID userId) {
		this.userId = userId;
		return this;
	}
	
	
}
