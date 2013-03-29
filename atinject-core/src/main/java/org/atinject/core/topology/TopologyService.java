package org.atinject.core.topology;

import java.util.ArrayList;
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
    @Inject @CacheName("distributed-executor") private Cache<Object, Object> masterCacheNode;

    @Inject private MachineIdUrlMapper urlMapper;
    
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
    
    public String getLocalUrl(){
        return getUrl(getLocalAddress().getMachineId());
    }
    
    public String getUrl(String machineId){
        return urlMapper.getUrl(machineId);
    }
    
    public List<String> getAllUrl(){
        List<Address> members = masterCacheNode.getAdvancedCache().getRpcManager().getMembers();
        List<String> topologyAwareAddresses = new ArrayList<>(members.size());
        for (Address member : members)
        {
            topologyAwareAddresses.add(getUrl(((TopologyAwareAddress) member).getMachineId()));
        }
        return topologyAwareAddresses;        
    }
    
    public List<TopologyAwareAddress> getAllAddress(){
        List<Address> members = masterCacheNode.getAdvancedCache().getRpcManager().getMembers();
        List<TopologyAwareAddress> topologyAwareAddresses = new ArrayList<>(members.size());
        for (Address member : members)
        {
            topologyAwareAddresses.add((TopologyAwareAddress) member);
        }
        return topologyAwareAddresses;
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
    
    // TODO get addresses, rackId
    
    // TODO get addresses, siteId
}
