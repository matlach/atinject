package org.atinject.core.exception;

import java.util.Arrays;
import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExceptionSanitizerService {

    public <T extends Throwable> T sanitize(T t, Collection<String> keywords) {
    	t.setStackTrace(
    			Arrays.stream(t.getStackTrace())
	        		.filter(stackTraceElement -> containsAnyKeyword(stackTraceElement, keywords))
	        		.toArray(StackTraceElement[]::new));
        return t;
    }
    
    private boolean containsAnyKeyword(StackTraceElement stackTraceElement, Collection<String> keywords) {
    	return keywords.parallelStream()
    			.anyMatch(keyword -> stackTraceElement.toString().contains(keyword));
    }
    
}
