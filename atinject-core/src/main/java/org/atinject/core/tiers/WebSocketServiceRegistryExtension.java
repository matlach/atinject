package org.atinject.core.tiers;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class WebSocketServiceRegistryExtension implements Extension {
    
    private Logger logger = LoggerFactory.getLogger(WebSocketServiceRegistryExtension.class);
    
    private List<Class<? extends WebSocketService>> classes;
    
    public WebSocketServiceRegistryExtension(){
        classes = new ArrayList<>();
    }
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event) {
        if (WebSocketService.class.isAssignableFrom(event.getAnnotatedType().getJavaClass())){
            classes.add((Class<WebSocketService>)event.getAnnotatedType().getJavaClass());
            logger.info("added '{}' to web socket service registry", event.getAnnotatedType().getJavaClass().getSimpleName());
        }
     }
    
    public List<Class<? extends WebSocketService>> getClasses()
    {
        return classes;
    }

}