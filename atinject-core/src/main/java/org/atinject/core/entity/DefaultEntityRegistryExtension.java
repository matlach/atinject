package org.atinject.core.entity;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.core.marshallable.AbstractRegistryExtension;

public class DefaultEntityRegistryExtension extends AbstractRegistryExtension<Entity> implements EntityRegistryExtension {
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<? extends Entity> event) {
        addClass(event.getAnnotatedType().getJavaClass());
     }
}