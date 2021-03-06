package org.atinject.api.usercredential.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.atinject.api.usercredential.UserCredentialService;
import org.atinject.core.cdi.CDI;

public class ExistingUsernameValidator implements ConstraintValidator<ExistingUsername, String> {

	private UserCredentialService userCredentialService;
	
	@Override
	public void initialize(ExistingUsername constraintAnnotation) {
		userCredentialService = CDI.select(UserCredentialService.class).get();
	}

	@Override
	public boolean isValid(String username, ConstraintValidatorContext context) {
		return userCredentialService.getUserCredential(username).isPresent();
	}
}
