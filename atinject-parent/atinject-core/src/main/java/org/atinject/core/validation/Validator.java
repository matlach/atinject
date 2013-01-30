package org.atinject.core.validation;

import java.lang.reflect.Method;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.ConstraintViolation;
import javax.validation.MethodValidator;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;

@ApplicationScoped
public class Validator
{
    private javax.validation.Validator validator;
    private MethodValidator methodValidator;

    @PostConstruct
    public void initialize()
    {
        HibernateValidatorConfiguration configuration = Validation.byProvider(HibernateValidator.class).configure();
        ValidatorFactory factory = configuration
                .failFast(true)
                .buildValidatorFactory();
        this.validator = factory.getValidator();
        this.methodValidator = validator.forMethods();
    }

    public <T> Set<ConstraintViolation<T>> validateParameters(T object, Method method, Object[] parameterValues)
    {
        return methodValidator.validateParameters(object, method, parameterValues);
    }

    public <T> Set<ConstraintViolation<T>> validateReturnValue(T object, Method method, Object returnValue)
    {
        return methodValidator.validateReturnValue(object, method, returnValue);
    }

    public <T> Set<ConstraintViolation<T>> validate(T object)
    {
        return validator.validate(object);
    }

}
