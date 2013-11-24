package org.atinject.core.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.Producer;

import org.atinject.core.cdi.CDI;
import org.atinject.core.cdi.Named;
import org.infinispan.configuration.cache.Configuration;

public class CacheExtension implements Extension
{

    private Map<String, Producer<Configuration>> configurationProducers;

    public CacheExtension() {
        configurationProducers = new LinkedHashMap<>();
    }
    
    public <T, X> void onProcessProducer(@Observes ProcessProducer<T, Configuration> event, BeanManager beanManager) {
        Named named = event.getAnnotatedMember().getAnnotation(Named.class);
        if (named == null) {
            throw new NullPointerException("@Named must be defined");
        }
        configurationProducers.put(named.value(), event.getProducer());
    }
    
    public Map<String, Configuration> getConfigurations() {
        Map<String, Configuration> configurations = new LinkedHashMap<>(configurationProducers.size());
        for (Entry<String, Producer<Configuration>> entry : configurationProducers.entrySet()){
            configurations.put(entry.getKey(), entry.getValue().produce(CDI.createUnboundCreationalContext()));
        }
        return configurations;
    }
    
}
