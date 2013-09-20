package org.atinject.api.userlockout;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.atinject.api.authentication.event.AuthenticationFailed;
import org.atinject.core.tiers.Service;

@ApplicationScoped
public class UserLockoutService extends Service {

    // Threshold 5times, duration 30mins, reset duration 5 minutes.
	
	public boolean isUserLocked(String userId){
		return false;
	}
	
	public void lockUser(String userId){
		
	}
	
	public void unlockUser(String userId){
		
	}
	
	public void onAuthenticationFailed(@Observes AuthenticationFailed event){
		// increment authentication failed count
	}
}
