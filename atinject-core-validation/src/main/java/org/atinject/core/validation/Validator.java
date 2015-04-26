package org.atinject.core.validation;

import java.lang.reflect.Method;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.executable.ExecutableValidator;

import org.atinject.core.cdi.CDI;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.parameternameprovider.ReflectionParameterNameProvider;

@ApplicationScoped
public class Validator {

    private javax.validation.Validator validator;
    private ExecutableValidator executableValidator;
    
    @PostConstruct
    public void initialize() {
        validator = Validation.byProvider(HibernateValidator.class)
        		.configure()
        		.parameterNameProvider(new ReflectionParameterNameProvider())
        		//TODO .messageInterpolator(interpolator)
        		.failFast(true)
        		.constraintValidatorFactory(new ConstraintValidatorFactory() {
					
					@Override
					public void releaseInstance(ConstraintValidator<?, ?> instance) {
						// no-op
					}
					
					@Override
					public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
						return CDI.select(key).get();
					}
				})
        		.buildValidatorFactory()
        		.getValidator();
        executableValidator = validator.forExecutables();
    }
    
    public <T> Set<ConstraintViolation<T>> validateParameters(T object, Method method, Object[] parameterValues) {
        return executableValidator.validateParameters(object, method, parameterValues);
    }

    public <T> Set<ConstraintViolation<T>> validateReturnValue(T object, Method method, Object returnValue) {
        return executableValidator.validateReturnValue(object, method, returnValue);
    }

    public <T> Set<ConstraintViolation<T>> validate(T object) {
        return validator.validate(object);
    }

}
