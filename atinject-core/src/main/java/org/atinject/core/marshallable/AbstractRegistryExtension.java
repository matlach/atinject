package org.atinject.core.marshallable;

import java.util.ArrayList;
import java.util.List;

import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public abstract class AbstractRegistryExtension<T> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    protected List<Class<? extends T>> classes;
    
    public AbstractRegistryExtension() {
        classes = new ArrayList<>();
    }
    
    public void addClass(Class<? extends T> clazz) {
        classes.add(clazz);
        logger.info("added '{}' to dto registry", clazz.getSimpleName());
    }
    
    public List<Class<? extends T>> getClasses() {
        return classes;
    }
}
