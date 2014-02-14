package org.atinject.core.concurrent;

import java.lang.reflect.Method;
import java.util.concurrent.Future;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

public class AsynchronousServiceExtension implements Extension {

    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event) {
        Class<T> clazz = event.getAnnotatedType().getJavaClass();
        for (Method method : clazz.getMethods()) {
            processMethod(method);
        }
     }
    
    private void processMethod(Method method) {
        if (method.isAnnotationPresent(Asynchronous.class)) {
            processAsynchronousMethod(method);
        }
    }
    
    private void processAsynchronousMethod(Method method){
        if (!void.class.isAssignableFrom(method.getReturnType()) &&
            !Future.class.isAssignableFrom(method.getReturnType())) {
            throw new RuntimeException("@Asynchronous annotated method return type must be either void or Future<?>");
        }
    }
}
