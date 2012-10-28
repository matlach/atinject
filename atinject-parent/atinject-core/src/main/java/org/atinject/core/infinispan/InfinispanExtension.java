package org.atinject.core.infinispan;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessProducer;

import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

public class InfinispanExtension implements Extension
{

    private Map<String, Configuration> configurations;

    private EmbeddedCacheManager cacheManager;
    
    public InfinispanExtension()
    {
        configurations = new HashMap<>();
    }
    
    public <T, X> void onProcessProducer(@Observes ProcessProducer<T, Configuration> event, BeanManager beanManager)
    {
        CacheName cacheName = event.getAnnotatedMember().getAnnotation(CacheName.class);
        if (cacheName == null)
        {
            throw new NullPointerException("@CacheName must be defined");
        }
        CreationalContext<Configuration> ctx = beanManager.createCreationalContext(null);
        Configuration cacheConfiguration = event.getProducer().produce(ctx);
        configurations.put(cacheName.value(), cacheConfiguration);
    }
    
    public void onAfterDeploymentValidation(@Observes AfterDeploymentValidation event)
    {
        cacheManager = newClusteredCacheManager();
    }
    
    public EmbeddedCacheManager newClusteredCacheManager() {
        DefaultCacheManager cacheManager = 
        new DefaultCacheManager(
            newGlobalConfiguration(),
            newDefaultCacheConfiguration()
        );
        // define all cache configuration
        for (Entry<String, Configuration> entry : configurations.entrySet())
        {
            cacheManager.defineConfiguration(entry.getKey(), entry.getValue());
        }
        // start all cache in parallel to prevent asymmetric cluster
        Set<String> cacheNames = configurations.keySet();
        cacheManager.startCaches(cacheNames.toArray(new String[cacheNames.size()]));
        return cacheManager;
    }
    
    protected GlobalConfiguration newGlobalConfiguration(){
        return new GlobalConfigurationBuilder().transport().defaultTransport().build();
    }
    
    protected Configuration newDefaultCacheConfiguration(){
        return new ConfigurationBuilder().eviction().maxEntries(7).build();
    }
    
    public EmbeddedCacheManager getCacheManager()
    {
        return cacheManager;
    }
}
