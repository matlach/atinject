package org.atinject.core.tiers;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;

import org.atinject.core.marshallable.AbstractRegistryExtension;

public class WebSocketServiceRegistryExtension extends AbstractRegistryExtension implements Extension {
    
    <T> void processAnnotatedType(@Observes @WithAnnotations(WebSocketService.class) ProcessAnnotatedType<?> event) {
        addClass(event.getAnnotatedType().getJavaClass());
     }

}