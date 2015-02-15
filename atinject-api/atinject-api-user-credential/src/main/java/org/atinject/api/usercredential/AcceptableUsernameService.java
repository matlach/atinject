package org.atinject.api.usercredential;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AcceptableUsernameService {

	public boolean isAcceptable(String username) {
		return true;
	}
}
