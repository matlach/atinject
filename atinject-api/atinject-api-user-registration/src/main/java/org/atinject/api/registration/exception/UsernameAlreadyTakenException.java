package org.atinject.api.registration.exception;

public class UsernameAlreadyTakenException extends RegistrationException {

    private static final long serialVersionUID = 1L;

	@Override
	public String getExceptionCode() {
		return RegistrationExceptionCodes.USERNAME_ALREADY_TAKEN.name();
	}

}
