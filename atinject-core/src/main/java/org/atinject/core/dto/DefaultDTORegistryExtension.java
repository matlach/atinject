package org.atinject.core.dto;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.core.marshallable.AbstractRegistryExtension;

public class DefaultDTORegistryExtension extends AbstractRegistryExtension implements DTORegistryExtension {
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<? extends DTO> event) {
        addClass(event.getAnnotatedType().getJavaClass());
     }

}
