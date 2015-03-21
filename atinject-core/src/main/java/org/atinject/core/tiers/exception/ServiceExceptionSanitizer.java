package org.atinject.core.tiers.exception;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.exception.ExceptionSanitizerService;

@ApplicationScoped
public class ServiceExceptionSanitizer {

	@Inject
	private ExceptionSanitizerService exceptionSanitizerService;
	
    private List<String> keywords;
    
    @PostConstruct
    public void initialize() {
    	keywords = new ArrayList<>();
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
    
    public <T extends Throwable> T sanitize(T t) {
    	return exceptionSanitizerService.sanitize(t, keywords);
    }
    
}
