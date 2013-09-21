package org.atinject.core.tiers;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class FactoryRegistryExtension implements Extension {
    
    private Logger logger = LoggerFactory.getLogger(FactoryRegistryExtension.class);
    
    private List<Class<? extends Factory>> classes;
    
    public FactoryRegistryExtension(){
        classes = new ArrayList<>();
    }
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<? extends Factory> event) {
        classes.add(event.getAnnotatedType().getJavaClass());
        logger.info("added '{}' to factory service registry", event.getAnnotatedType().getJavaClass().getSimpleName());
     }
    
    public List<Class<? extends Factory>> getClasses()
    {
        return classes;
    }

}