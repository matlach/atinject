package org.atinject.core.entity;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class EntityRegistryExtension implements Extension
{
    private Logger logger = LoggerFactory.getLogger(EntityRegistryExtension.class);
    
    private List<Class<? extends Entity>> entityClasses;
    
    public EntityRegistryExtension(){
        entityClasses = new ArrayList<>();
    }
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event) {
        if (Entity.class.isAssignableFrom(event.getAnnotatedType().getJavaClass())){
            entityClasses.add((Class<Entity>)event.getAnnotatedType().getJavaClass());
            logger.info("added '{}' to entity registry", event.getAnnotatedType().getJavaClass().getSimpleName());
        }
     }
    
    public List<Class<? extends Entity>> getEntityClasses()
    {
        return entityClasses;
    }

}