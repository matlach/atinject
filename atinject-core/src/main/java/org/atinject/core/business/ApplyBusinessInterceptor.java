package org.atinject.core.business;

import java.lang.reflect.Parameter;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.atinject.core.cdi.CDI;

// TODO this should run before Validation interceptor
@ApplyBusiness
@Interceptor @Priority(Interceptor.Priority.APPLICATION)
public class ApplyBusinessInterceptor {

    @AroundInvoke
    public Object validateMethod(InvocationContext ctx) throws Exception {
    	Object[] newParameters = ctx.getParameters();
    	int i = 0;
    	for (Parameter parameter : ctx.getMethod().getParameters()) {
    		// TODO make sure this is in the correct order
    		for (Business businessAnnotation : parameter.getAnnotationsByType(Business.class)) {
    			// TODO make sure we handle generic properly
    			BusinessExecutor<?, Object> businessExecutor = (BusinessExecutor<?, Object>) CDI.select(businessAnnotation.executedBy());
    			newParameters[i] = businessExecutor.execute(newParameters[i]);
    		}
    		i = i + 1;
    	}
    	ctx.setParameters(newParameters);
        return ctx.proceed();
    }
}
