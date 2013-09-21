package org.atinject.core.tiers;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class CacheStoreRegistryExtension implements Extension {
    
    private Logger logger = LoggerFactory.getLogger(CacheStoreRegistryExtension.class);
    
    private List<Class<? extends CacheStore>> classes;
    
    public CacheStoreRegistryExtension(){
        classes = new ArrayList<>();
    }
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<? extends CacheStore> event) {
        classes.add(event.getAnnotatedType().getJavaClass());
        logger.info("added '{}' to cache store service registry", event.getAnnotatedType().getJavaClass().getSimpleName());
     }
    
    public List<Class<? extends CacheStore>> getClasses()
    {
        return classes;
    }

}