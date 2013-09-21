package org.atinject.core.tiers;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class ServiceRegistryExtension implements Extension {
    
    private Logger logger = LoggerFactory.getLogger(ServiceRegistryExtension.class);
    
    private List<Class<? extends Service>> classes;
    
    public ServiceRegistryExtension(){
        classes = new ArrayList<>();
    }
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<? extends Service> event) {
        classes.add(event.getAnnotatedType().getJavaClass());
        logger.info("added '{}' to service registry", event.getAnnotatedType().getJavaClass().getSimpleName());
     }
    
    public List<Class<? extends Service>> getClasses()
    {
        return classes;
    }

}