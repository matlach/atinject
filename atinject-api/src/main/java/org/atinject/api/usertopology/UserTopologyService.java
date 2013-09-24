package org.atinject.api.usertopology;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.api.user.entity.UserEntity;
import org.atinject.core.cdi.Named;
import org.infinispan.Cache;
import org.infinispan.remoting.transport.TopologyAwareAddress;

@ApplicationScoped
public class UserTopologyService
{

    @Inject @Named("user") private Cache<String, UserEntity> userCache;
    
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
