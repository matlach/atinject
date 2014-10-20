package org.atinject.api.userregistrationthrottling;

public class UserRegistrationThrottlingService {

	private boolean throttling;
	
	public boolean isThrottling() {
		return throttling;
	}
	
	public void enableThrottling() {
		throttling = true;
	}
	
	public void disableThrottling() {
		throttling = false;
	}
}
