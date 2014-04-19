package org.atinject.core.topology;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.infinispan.remoting.transport.TopologyAwareAddress;

@ApplicationScoped
public class TopologyService {
    
    @Inject @Named("distributed-executor")
    private DistributedCache<Object, Object> masterCacheNode;

    @Inject
    private MachineIdUrlMapper urlMapper;
    
    @PostConstruct
    public void initialize() {
        // ensure we're a topology aware cluster
        if ( ! (masterCacheNode.getRpcManager().getAddress() instanceof TopologyAwareAddress)) {
            throw new RuntimeException("must be in a topology aware cluster");
        }
    }
    
    public TopologyAwareAddress getLocalAddress() {
        return (TopologyAwareAddress) masterCacheNode.getRpcManager().getAddress();
    }
    
    public String getLocalMachineId(){
        return getLocalAddress().getMachineId();
    }
    
    public String getLocalRackId(){
        return getLocalAddress().getRackId();
    }
    
    public String getLocalSiteId(){
        return getLocalAddress().getSiteId();
    }
    
    public String getLocalUrl(){
        return getUrl(getLocalMachineId());
    }
    
    public String getUrl(String machineId){
        return urlMapper.getUrl(machineId);
    }
    
    public List<String> getAllUrl() {
    	return masterCacheNode.getRpcManager().getMembers().stream()
    			.map(member -> (TopologyAwareAddress) member)
    			.map(member -> getUrl(member.getMachineId()))
    			.collect(Collectors.toList());
    }
    
    public List<TopologyAwareAddress> getAllRemoteAddress() {
    	return masterCacheNode.getRpcManager().getMembers().stream()
    			.map(member -> (TopologyAwareAddress) member)
    			.filter(member -> member.equals(getLocalAddress()))
    			.collect(Collectors.toList());
    }
    
    public List<TopologyAwareAddress> getAllAddress() {
        return masterCacheNode.getRpcManager().getMembers().stream()
        		.map(member -> (TopologyAwareAddress) member)
        		.collect(Collectors.toList());
    }
    
    public List<TopologyAwareAddress> getAllAddressByRackId(String rackId) {
        return masterCacheNode.getRpcManager().getMembers().stream()
        		.map(member -> (TopologyAwareAddress) member)
        		.filter(member -> member.getRackId().equals(rackId))
        		.collect(Collectors.toList());
    }
    
    public List<TopologyAwareAddress> getAllAddressBySiteId(String siteId) {
    	return masterCacheNode.getRpcManager().getMembers().stream()
    			.map(member -> (TopologyAwareAddress) member)
    			.filter(member -> member.getSiteId().equals(siteId))
    			.collect(Collectors.toList());
    }
    
    public TopologyAwareAddress getAddress(String machineId) {
    	return masterCacheNode.getRpcManager().getMembers().stream()
    			.map(member -> (TopologyAwareAddress) member)
    			.filter(member -> member.getMachineId().equals(machineId))
    			.findFirst()
    			.orElse(null);
    }
    
    public List<String> getAllMachineIdBySiteId(String siteId) {
    	return masterCacheNode.getRpcManager().getMembers().stream()
    			.map(member -> (TopologyAwareAddress) member)
    			.filter(member -> member.getSiteId().equals(siteId))
    			.map(member -> member.getMachineId())
    			.collect(Collectors.toList());
    }
    
    public List<String> getAllMachineIdByRackId(String rackId) {
    	return masterCacheNode.getRpcManager().getMembers().stream()
    			.map(member -> (TopologyAwareAddress) member)
    			.filter(member -> member.getRackId().equals(rackId))
    			.map(member -> member.getMachineId())
    			.collect(Collectors.toList());
    }
    
}
