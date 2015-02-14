package org.atinject.api.user.validation;

import java.util.UUID;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.atinject.api.user.UserService;
import org.atinject.core.cdi.CDI;

public class NonExistingUserIdValidator implements ConstraintValidator<ExistingUserId, UUID> {

	private UserService userService;
	
	@Override
	public void initialize(ExistingUserId constraintAnnotation) {
		userService = CDI.select(UserService.class).get();
	}

	@Override
	public boolean isValid(UUID userId, ConstraintValidatorContext context) {
		return !userService.getUser(userId).isPresent();
	}

}
