package org.atinject.api.registration;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.api.usercredential.entity.UserCredentialEntity;

public class RegistratedUser {

	private UserEntity user;
	private UserCredentialEntity userCredential;
	
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	public UserCredentialEntity getUserCredential() {
		return userCredential;
	}
	public void setUserCredential(UserCredentialEntity userCredential) {
		this.userCredential = userCredential;
	}
	
	
}
