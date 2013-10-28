package org.atinject.api.analytic;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.atinject.api.registration.event.UserRegistered;

@ApplicationScoped
public class GuestRegistrationAnalyticService {

	public void onUserRegistered(@Observes UserRegistered event) {
		// TODO
	}
}
