package org.atinject.core.tiers;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

import org.atinject.core.marshallable.AbstractRegistryExtension;

public class FactoryRegistryExtension extends AbstractRegistryExtension<Factory> implements Extension {
    
    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<? extends Factory> event) {
        addClass(event.getAnnotatedType().getJavaClass());
     }

}