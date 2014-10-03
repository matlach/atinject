package org.atinject.core.tiers.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ServiceExceptionSanitizer {

    private List<String> keywords = new ArrayList<>();
    
    @PostConstruct
    public void initialize() {
        keywords.add("sun.reflect.NativeMethodAccessorImpl.invoke");
        keywords.add("sun.reflect.DelegatingMethodAccessorImpl.invoke");
        
        keywords.add("java.lang.reflect.Method.invoke");
        
        keywords.add("$Proxy$_$$_");
        keywords.add("org.jboss.weld.bean.proxy");
        keywords.add("org.jboss.weld.interceptor.proxy");
        keywords.add("org.jboss.weld.interceptor.chain.AbstractInterceptionChain.invokeNext");
        
        keywords.add("org.junit");
        keywords.add("org.eclipse.jdt.internal.junit");
    }
    
    public Exception sanitize(Exception e) {
    	e.setStackTrace(
    			Arrays.stream(e.getStackTrace())
	        		.filter(stackTraceElement -> containsAnyKeyword(stackTraceElement))
	        		.toArray(StackTraceElement[]::new));
        return e;
    }
    
    private boolean containsAnyKeyword(StackTraceElement stackTraceElement) {
    	return keywords.parallelStream()
    			.anyMatch(keyword -> stackTraceElement.toString().contains(keyword));
    }
}
