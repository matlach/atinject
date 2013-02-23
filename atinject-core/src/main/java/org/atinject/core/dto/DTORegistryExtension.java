package org.atinject.core.dto;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.core.logging.LoggerFactory;
import org.slf4j.Logger;

public class DTORegistryExtension implements Extension
{
    private Logger logger = LoggerFactory.getLogger(DTORegistryExtension.class);
    
    private List<Class<? extends DTO>> dtoClasses;
    
    public DTORegistryExtension(){
        dtoClasses = new ArrayList<>();
    }
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event) {
        if (DTO.class.isAssignableFrom(event.getAnnotatedType().getJavaClass())){
            dtoClasses.add((Class<DTO>)event.getAnnotatedType().getJavaClass());
            logger.info("added '{}' to dto registry", event.getAnnotatedType().getJavaClass().getSimpleName());
        }
     }
    
    public List<Class<? extends DTO>> getDTOClasses()
    {
        return dtoClasses;
    }

}
