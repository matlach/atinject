package org.atinject.core.validation;

import java.util.Set;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


@Validate
@Interceptor
public class ValidateInterceptor {

    @Inject
    private Validator validator;

    @AroundInvoke
    public Object validateMethod(InvocationContext ctx) throws Exception {
        // TODO do not validate Session, only request
        
        Set<ConstraintViolation<Object>> violations = validator.validateParameters(ctx.getTarget(), ctx.getMethod(), ctx.getParameters());

        if (! violations.isEmpty()) {
        	StringBuilder message = new StringBuilder();
        	for (ConstraintViolation<Object> violation : violations) {
        		message.append(violation.getMessage()).append("\n");
        	}
        	message.setLength(message.length() - 1);
            throw new ConstraintViolationException(message.toString(), violations);
        }

        return ctx.proceed();
    }

}