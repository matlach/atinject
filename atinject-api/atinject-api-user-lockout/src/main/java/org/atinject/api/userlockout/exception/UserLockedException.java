package org.atinject.api.userlockout.exception;

import org.atinject.core.exception.ApplicationException;

public class UserLockedException extends ApplicationException {

    private static final long serialVersionUID = 1L;

	@Override
	public String getExceptionCode() {
		return null;
	}

}
