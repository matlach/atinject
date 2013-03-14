package org.atinject.core.validation;

import java.util.Set;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


@ValidateMethod
@Interceptor
public class ValidateRequestInterceptor
{

    @Inject
    private Validator validator;

    @AroundInvoke
    public Object validateMethod(InvocationContext ctx) throws Exception
    {
        Set<ConstraintViolation<Object>> violations = validator.validateParameters(ctx.getTarget(), ctx.getMethod(), ctx.getParameters());

        if (! violations.isEmpty())
        {
            throw new ConstraintViolationException(violations);
        }

        Object result = ctx.proceed();

        return result;
    }

}