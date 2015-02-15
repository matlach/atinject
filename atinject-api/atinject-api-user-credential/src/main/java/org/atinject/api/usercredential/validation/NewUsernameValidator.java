package org.atinject.api.usercredential.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class NewUsernameValidator implements ConstraintValidator<NewUsername, Object[]> {

	@Override
	public void initialize(NewUsername constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(Object[] usernameNewUsername, ConstraintValidatorContext context) {
		if (usernameNewUsername.length != 2 ||
				!(usernameNewUsername[0] instanceof String) ||
				!(usernameNewUsername[1] instanceof String)) {
			throw new IllegalArgumentException("Illegal method signature");
		}
		String username = (String) usernameNewUsername[0];
		String newUsername = (String) usernameNewUsername[1];
		return !username.equals(newUsername);
	}

}