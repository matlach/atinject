package org.atinject.core.distexec;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.cache.CacheName;
import org.infinispan.Cache;
import org.infinispan.remoting.transport.TopologyAwareAddress;

@ApplicationScoped
public class UserTopologyService
{

    @Inject
    @CacheName("user")
    private Cache<?, ?> userCache;
    
    @PostConstruct
    public void initialize(){
        // ensure we're a topology aware cluster
        if ( ! (userCache.getAdvancedCache().getRpcManager().getAddress() instanceof TopologyAwareAddress))
        {
            throw new RuntimeException("must be in a topology aware cluster");
        }
        if (userCache.getAdvancedCache().getDistributionManager() == null){
            throw new RuntimeException("must be a distributed cache");
        }
    }
    
    public TopologyAwareAddress getUserKeyPrimaryLocation(String userId){
        return (TopologyAwareAddress) userCache.getAdvancedCache().getDistributionManager().getPrimaryLocation(userId);
    }
    
}
