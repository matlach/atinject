package org.atinject.userauthenticationchallenge;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.usersession.UserSession;
import org.atinject.api.usersession.UserSessionService;
import org.atinject.core.concurrent.ScheduledService;
import org.atinject.core.session.event.SessionOpened;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class UserAuthenticationChallengeService extends Service {

	@Inject
	private ScheduledService scheduledService;
	
	@Inject
	private UserSessionService userSessionService;
	
	private long challengeDelay;
	
	@PostConstruct
	public void initialize() {
		challengeDelay = 3000;
	}
	
	public void onSessionOpened(@Observes SessionOpened event) {
		scheduledService.schedule(
				() -> disconnectIfNotAuthenticated(event.getSession().getSessionId()),
				challengeDelay,
				TimeUnit.MILLISECONDS);
	}
	
	public void disconnectIfNotAuthenticated(String sessionId) {
		UserSession userSession = userSessionService.getSession(sessionId);
		if (userSession == null) {
			return;
		}
		if (userSession.getUserId() == null) {
			userSessionService.closeSession(userSession);
		}
	}
}
