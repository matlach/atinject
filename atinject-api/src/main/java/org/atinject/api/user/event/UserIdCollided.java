package org.atinject.api.user.event;

public class UserIdCollided {

	private String userId;

	public String getUserId() {
		return userId;
	}

	public UserIdCollided setUserId(String userId) {
		this.userId = userId;
		return this;
	}
	
	
}
