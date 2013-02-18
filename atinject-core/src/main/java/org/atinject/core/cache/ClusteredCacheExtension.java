package org.atinject.core.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.Producer;

import org.atinject.core.cdi.BeanManagerExtension;
import org.infinispan.configuration.cache.Configuration;

public class ClusteredCacheExtension implements Extension
{

    private Map<String, Producer<Configuration>> configurationProducers;

    public ClusteredCacheExtension()
    {
        configurationProducers = new LinkedHashMap<>();
    }
    
    public <T, X> void onProcessProducer(@Observes ProcessProducer<T, Configuration> event, BeanManager beanManager)
    {
        CacheName cacheName = event.getAnnotatedMember().getAnnotation(CacheName.class);
        if (cacheName == null)
        {
            throw new NullPointerException("@CacheName must be defined");
        }
        configurationProducers.put(cacheName.value(), event.getProducer());
    }
    
    public Map<String, Configuration> getConfigurations(){
        Map<String, Configuration> configurations = new LinkedHashMap<>(configurationProducers.size());
        for (Entry<String, Producer<Configuration>> entry : configurationProducers.entrySet()){
            
            configurations.put(entry.getKey(), entry.getValue().produce(BeanManagerExtension.createUnboundCreationalContext()));
        }
        return configurations;
    }
    
}