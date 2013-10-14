package org.atinject.api.user.event;

import org.atinject.core.event.Event;

public class UserRelocated extends Event {

	private static final long serialVersionUID = 1L;

	private String oldUserId;
	
	private String newUserId;

	public String getOldUserId() {
		return oldUserId;
	}

	public UserRelocated setOldUserId(String oldUserId) {
		this.oldUserId = oldUserId;
		return this;
	}

	public String getNewUserId() {
		return newUserId;
	}

	public UserRelocated setNewUserId(String newUserId) {
		this.newUserId = newUserId;
		return this;
	}
	
}
