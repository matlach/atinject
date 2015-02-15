package org.atinject.api.usercredential.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class NewPasswordValidator implements ConstraintValidator<NewPassword, Object[]> {

	@Override
	public void initialize(NewPassword constraintAnnotation) {
		
	}

	@Override
	public boolean isValid(Object[] passwordNewPassword, ConstraintValidatorContext context) {
		if (passwordNewPassword.length != 3 ||
				!(passwordNewPassword[1] instanceof String) ||
				!(passwordNewPassword[2] instanceof String)) {
			throw new IllegalArgumentException("Illegal method signature");
		}
		String password = (String) passwordNewPassword[1];
		String newPassword = (String) passwordNewPassword[2];
		return !password.equals(newPassword);
	}

}
