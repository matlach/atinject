package org.atinject.api.usercredential.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.atinject.api.usercredential.AcceptableUsernameService;
import org.atinject.core.cdi.CDI;

public class AcceptableUsernameValidator implements ConstraintValidator<AcceptableUsername, String> {

	private AcceptableUsernameService acceptableUsernameService;
	
	@Override
	public void initialize(AcceptableUsername constraintAnnotation) {
		acceptableUsernameService = CDI.select(AcceptableUsernameService.class).get();
	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		return acceptableUsernameService.isAcceptable(username);
	}
	
}
