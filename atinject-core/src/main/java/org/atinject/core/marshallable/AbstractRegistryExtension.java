package org.atinject.core.marshallable;

import java.util.ArrayList;
import java.util.List;

import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public abstract class AbstractRegistryExtension<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    protected List<Class<T>> classes;
    
    public AbstractRegistryExtension() {
        classes = new ArrayList<>();
    }
    
    public void addClass(Class<T> clazz) {
        classes.add(clazz);
        logger.info("added '{}' to registry", clazz.getSimpleName());
    }
    
    public List<Class<T>> getClasses() {
        return classes;
    }
}
