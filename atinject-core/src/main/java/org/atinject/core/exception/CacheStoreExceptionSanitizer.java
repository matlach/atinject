package org.atinject.core.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CacheStoreExceptionSanitizer {

    List<String> keywords = new ArrayList<>();
    
    @PostConstruct
    public void initialize()
    {
        keywords.add("$_$$_weld");
        keywords.add("org.jboss.weld.interceptor.proxy");
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
