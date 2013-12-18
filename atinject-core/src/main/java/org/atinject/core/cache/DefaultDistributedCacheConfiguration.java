package org.atinject.core.cache;

import javax.enterprise.context.ApplicationScoped;

import org.infinispan.configuration.cache.ConfigurationBuilder;

@ApplicationScoped
public class DefaultDistributedCacheConfiguration extends AbstractCacheConfiguration {

    @Override
    public ConfigurationBuilder get() {
        ConfigurationBuilder configuration = new ConfigurationBuilder();
        withDistributedClustering(configuration);
        return configuration;
    }

    
}
