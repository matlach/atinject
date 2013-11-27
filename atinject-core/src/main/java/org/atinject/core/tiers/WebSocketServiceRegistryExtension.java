package org.atinject.core.tiers;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.core.marshallable.AbstractRegistryExtension;

public class WebSocketServiceRegistryExtension extends AbstractRegistryExtension<WebSocketService> implements Extension {
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<? extends WebSocketService> event) {
        addClass(event.getAnnotatedType().getJavaClass());
     }

}