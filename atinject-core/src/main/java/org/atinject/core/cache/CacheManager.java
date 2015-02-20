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
public class CacheManager
{

    @Inject
    private CacheExtension cacheExtension;
    
    private EmbeddedCacheManager cacheManager;
    
    @PostConstruct
    public void initialize() {
        cacheManager = new DefaultCacheManager(newGlobalConfiguration(), newDefaultCacheConfiguration());
        // add listener
        cacheManager.addListener(new ClusteredCacheManagerListener());

        // define all cache configuration
        for (Entry<String, Configuration> entry : cacheExtension.getConfigurations().entrySet()) {
            cacheManager.defineConfiguration(entry.getKey(), entry.getValue());
        }

        // start all cache in parallel to prevent asymmetric cluster
        Set<String> cacheNames = cacheExtension.getConfigurations().keySet();
        cacheManager.startCaches(cacheNames.toArray(new String[cacheNames.size()]));
        for (String cacheName : cacheNames) {
            Cache<?, ?> cache = cacheManager.getCache(cacheName);
            cache.addListener(new ClusteredCacheListener<>());
        }
    }

    // these settings must be configurable, system properties ???
    protected GlobalConfiguration newGlobalConfiguration(){
    	System.getProperty("org.atinject.cluster.name");
    	System.getProperty("org.atinject.cluster.machine.id");
    	System.getProperty("org.atinject.cluster.rack.id");
    	System.getProperty("org.atinject.cluster.site.id");
        return new GlobalConfigurationBuilder()
        	.globalJmxStatistics()
        		.allowDuplicateDomains(Boolean.TRUE)
        		.enable()
            .transport()
                .defaultTransport()
                .addProperty("configurationFile", "jgroups.xml")
                .clusterName("atinject")
                .machineId("localhost")
                .rackId("no-rack")
                .siteId("no-site")
            .build();
    }
    
    protected Configuration newDefaultCacheConfiguration() {
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
