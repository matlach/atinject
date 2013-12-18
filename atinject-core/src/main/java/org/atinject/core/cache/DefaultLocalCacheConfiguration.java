package org.atinject.core.cache;

import javax.enterprise.context.ApplicationScoped;

import org.infinispan.configuration.cache.ConfigurationBuilder;

@ApplicationScoped
public class DefaultLocalCacheConfiguration extends AbstractCacheConfiguration {

    @Override
    public ConfigurationBuilder get() {
        ConfigurationBuilder configuration = new ConfigurationBuilder();
        withOptimisticTransaction(configuration);
        return configuration;
    }
    
}
