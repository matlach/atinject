package org.atinject.core.cache;

import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

@ApplicationScoped
public class ClusteredCacheManager
{

    @Inject
    private ClusteredCacheExtension infinispanExtension;
    
    private EmbeddedCacheManager cacheManager;
    
    @PostConstruct
    public void initialize(){
        cacheManager = new DefaultCacheManager(newGlobalConfiguration(), newDefaultCacheConfiguration());
        // add listener
        cacheManager.addListener(new ClusteredCacheManagerListener());

        // define all cache configuration
        for (Entry<String, Configuration> entry : infinispanExtension.getConfigurations().entrySet())
        {
            cacheManager.defineConfiguration(entry.getKey(), entry.getValue());
        }

        // start all cache in parallel to prevent asymmetric cluster
        Set<String> cacheNames = infinispanExtension.getConfigurations().keySet();
        cacheManager.startCaches(cacheNames.toArray(new String[cacheNames.size()]));
        for (String cacheName : cacheNames)
        {
            Cache<?, ?> cache = cacheManager.getCache(cacheName);
            cache.addListener(new ClusteredCacheListener<>());
        }
    }

    protected GlobalConfiguration newGlobalConfiguration(){
        return new GlobalConfigurationBuilder()
            .transport()
                .defaultTransport()
                .clusterName("clusterName")
                .machineId("machineId")
                .rackId("rackId")
                .siteId("siteId")
            .build();
    }
    
    protected Configuration newDefaultCacheConfiguration(){
        return new ConfigurationBuilder().build();
    }
    
    public <K, V> Cache<K, V> getCache(String cacheName){
        return cacheManager.getCache(cacheName, false);
    }
    
    @PreDestroy
    public void cleanUp(){
        cacheManager.stop();
    }
}
