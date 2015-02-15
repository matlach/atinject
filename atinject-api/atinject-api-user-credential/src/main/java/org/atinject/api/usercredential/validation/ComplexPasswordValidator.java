package org.atinject.api.usercredential.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.atinject.api.usercredential.PasswordComplexityService;
import org.atinject.core.cdi.CDI;

public class ComplexPasswordValidator implements ConstraintValidator<ComplexPassword, String> {

	private PasswordComplexityService passwordComplexityService;
	
	@Override
	public void initialize(ComplexPassword constraintAnnotation) {
		passwordComplexityService = CDI.select(PasswordComplexityService.class).get();
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		return passwordComplexityService.isComplex(password);
	}
}
