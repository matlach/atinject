package org.atinject.api.authentication.exception;

import org.atinject.core.exception.ApplicationException;

public class WrongPasswordException extends ApplicationException {

    private static final long serialVersionUID = 1L;

	@Override
	public String getExceptionCode() {
		return AuthenticationExceptionCodes.WRONG_PASSWORD.name();
	}

}
