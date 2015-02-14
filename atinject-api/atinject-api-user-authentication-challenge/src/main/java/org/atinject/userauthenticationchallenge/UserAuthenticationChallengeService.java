package org.atinject.userauthenticationchallenge;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.atinject.api.usersession.UserSession;
import org.atinject.api.usersession.UserSessionService;
import org.atinject.core.concurrent.ScheduledService;
import org.atinject.core.session.event.SessionOpened;
import org.atinject.core.tiers.Service;

@Service
public class UserAuthenticationChallengeService {

	@Inject
	private ScheduledService scheduledService;
	
	@Inject
	private UserSessionService userSessionService;
	
	private long challengeDelayMillis;
	
	@PostConstruct
	public void initialize() {
		challengeDelayMillis = 3000;
	}
	
	public void onSessionOpened(@Observes SessionOpened event) {
		scheduledService.schedule(
				() -> disconnectIfNotAuthenticated(event.getSession().getSessionId()),
				challengeDelayMillis,
				TimeUnit.MILLISECONDS);
	}
	
	public void disconnectIfNotAuthenticated(String sessionId) {
		Optional<UserSession> userSession = userSessionService.getSession(sessionId);
		if (userSession.isPresent() && userSession.get().getUserId() == null) {
			userSessionService.closeSession(userSession.get());
		}
	}
}
