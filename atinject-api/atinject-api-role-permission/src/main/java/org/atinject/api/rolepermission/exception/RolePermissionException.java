package org.atinject.api.rolepermission.exception;

import org.atinject.core.exception.ApplicationException;

public class RolePermissionException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public RolePermissionException() {
        super();
    }

    public RolePermissionException(String message) {
        super(message);
    }

    public RolePermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RolePermissionException(Throwable cause) {
        super(cause);
    }

	@Override
	public String getExceptionCode() {
		return null;
	}
}
