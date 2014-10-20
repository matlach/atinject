package org.atinject.api.userregistrationthrottling;

import javax.inject.Inject;

public abstract class UserRegistrationServiceThrottlingDecorator {

	@Inject private UserRegistrationThrottlingService userRegistrationThrottlingService;
	
	public void register() {
		if (userRegistrationThrottlingService.isThrottling()) {
			throw new RuntimeException();
		}
		// else proceed
	}
}
