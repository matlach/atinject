package org.atinject.core.validation;

import java.lang.reflect.Method;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.executable.ExecutableValidator;

@ApplicationScoped
public class Validator
{
    
    @Inject private javax.validation.Validator validator;
    @Inject private ExecutableValidator executableValidator;

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
