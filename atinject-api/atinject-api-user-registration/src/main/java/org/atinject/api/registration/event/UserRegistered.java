package org.atinject.api.registration.event;

import org.atinject.api.registration.RegistratedUser;
import org.atinject.core.event.Event;

public class UserRegistered extends Event {

    private static final long serialVersionUID = 1L;

    private RegistratedUser registratedUser;

	public RegistratedUser getRegistratedUser() {
		return registratedUser;
	}

	public UserRegistered setRegistratedUser(RegistratedUser registratedUser) {
		this.registratedUser = registratedUser;
		return this;
	}
}
