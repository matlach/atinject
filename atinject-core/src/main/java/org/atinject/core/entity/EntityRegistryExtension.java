package org.atinject.core.entity;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class EntityRegistryExtension implements Extension {
    
    private Logger logger = LoggerFactory.getLogger(EntityRegistryExtension.class);
    
    private List<Class<? extends Entity>> classes;
    
    public EntityRegistryExtension(){
        classes = new ArrayList<>();
    }
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<? extends Entity> event) {
        classes.add(event.getAnnotatedType().getJavaClass());
        logger.info("added '{}' to entity registry", event.getAnnotatedType().getJavaClass().getSimpleName());
     }
    
    public List<Class<? extends Entity>> getClasses()
    {
        return classes;
    }

}