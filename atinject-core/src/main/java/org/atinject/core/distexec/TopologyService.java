package org.atinject.core.distexec;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.cache.CacheName;
import org.infinispan.Cache;
import org.infinispan.remoting.transport.Address;
import org.infinispan.remoting.transport.TopologyAwareAddress;

@ApplicationScoped
public class TopologyService
{
    @Inject @CacheName("distributed-executor-master-cache-node") private Cache<Object, Object> masterCacheNode;

    @PostConstruct
    public void initialize(){
        // ensure we're a topology aware cluster
        if ( ! (masterCacheNode.getAdvancedCache().getRpcManager().getAddress() instanceof TopologyAwareAddress))
        {
            throw new RuntimeException("must be in a topology aware cluster");
        }
    }
    
    public TopologyAwareAddress getLocalAddress(){
        return (TopologyAwareAddress) masterCacheNode.getAdvancedCache().getRpcManager().getAddress();
    }
    
    public TopologyAwareAddress getAddress(String machineId){
        List<Address> members = masterCacheNode.getAdvancedCache().getRpcManager().getMembers();
        for (Address member : members)
        {
            TopologyAwareAddress topologyAwareMember = (TopologyAwareAddress) member;
            if (topologyAwareMember.getMachineId().equals(machineId)){
                return topologyAwareMember;
            }
        }
        return null;
    }
}
