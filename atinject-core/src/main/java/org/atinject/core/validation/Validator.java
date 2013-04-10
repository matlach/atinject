package org.atinject.core.validation;

import java.lang.reflect.Method;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.executable.ExecutableValidator;

@ApplicationScoped
public class Validator
{

    // XXX find why injection is not working anymore ?
    private javax.validation.Validator validator;
    private ExecutableValidator executableValidator;
    
    @PostConstruct
    public void initialize(){
        validator = Validation.buildDefaultValidatorFactory().getValidator();
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
