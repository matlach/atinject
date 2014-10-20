package org.atinject.core.tiers;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

import org.atinject.core.marshallable.AbstractRegistryExtension;

public class ServiceRegistryExtension extends AbstractRegistryExtension implements Extension {
    
    <T> void processAnnotatedType(@Observes @WithAnnotations(Service.class) ProcessAnnotatedType<?> event) {
        addClass(event.getAnnotatedType().getJavaClass());
     }

}