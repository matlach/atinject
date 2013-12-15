package org.atinject.core.tiers.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CacheExceptionSanitizer {

    List<String> keywords = new ArrayList<>();
    
    @PostConstruct
    public void initialize()
    {
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
    
    public Exception sanitize(Exception e){
        StackTraceElement[] stackTrace = new StackTraceElement[e.getStackTrace().length];
        int i = 0;
        for (StackTraceElement stackTraceElement : e.getStackTrace()){
            String stackTraceElementString = stackTraceElement.toString();
            boolean retainStackTraceElement = true;
            for (String keyword : keywords){
                if (stackTraceElementString.contains(keyword)){
                    retainStackTraceElement = false;
                    break;
                }
            }
            if (retainStackTraceElement){
                stackTrace[i] = stackTraceElement;
                i++;
            }
        }
        e.setStackTrace(Arrays.copyOfRange(stackTrace, 0, i));
        return e;
    }
}
