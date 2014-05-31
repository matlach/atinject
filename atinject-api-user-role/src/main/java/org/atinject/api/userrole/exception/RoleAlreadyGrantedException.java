package org.atinject.api.userrole.exception;

public class RoleAlreadyGrantedException extends UserRoleException {

	private static final long serialVersionUID = 1L;

	@Override
	public String getExceptionCode() {
		return UserRoleExceptionCodes.ROLE_ALREADY_GRANTED;
	}
	
}
