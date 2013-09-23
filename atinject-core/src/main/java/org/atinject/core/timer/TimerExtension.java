package org.atinject.core.timer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class TimerExtension implements Extension {

    private Logger logger = LoggerFactory.getLogger(TimerExtension.class);
    
    private List<Method> methods;
    
    public TimerExtension(){
        methods = new ArrayList<>();
    }
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event) {
        Class<?> clazz = event.getAnnotatedType().getJavaClass();
        for (Method method : clazz.getMethods()){
            if (method.isAnnotationPresent(Schedule.class)){
                methods.add(method);
                // TODO ensure method return void, and contains no argument
                logger.info("method '{}' is annotated by @schedule", method);
            }
        }
     }
    
    public List<Method> getScheduleAnnotatedMethods() {
        return methods;
    }
}
