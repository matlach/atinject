package org.atinject.core.dto;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class DefaultDTORegistryExtension implements DTORegistryExtension {
    
    private Logger logger = LoggerFactory.getLogger(DefaultDTORegistryExtension.class);
    
    private List<Class<? extends DTO>> classes;
    
    public DefaultDTORegistryExtension(){
        classes = new ArrayList<>();
    }
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<? extends DTO> event) {
        classes.add(event.getAnnotatedType().getJavaClass());
        logger.info("added '{}' to dto registry", event.getAnnotatedType().getJavaClass().getSimpleName());
     }
    
    @Override
    public List<Class<? extends DTO>> getClasses()
    {
        return classes;
    }

}
