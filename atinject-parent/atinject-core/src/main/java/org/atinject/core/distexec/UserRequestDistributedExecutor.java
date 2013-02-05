package org.atinject.core.distexec;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.cache.CacheName;
import org.atinject.core.concurrent.AsynchronousService;
import org.infinispan.Cache;
import org.infinispan.distexec.DefaultExecutorService;
import org.infinispan.distexec.DistributedExecutorService;
import org.infinispan.remoting.transport.Address;
import org.infinispan.remoting.transport.TopologyAwareAddress;

@ApplicationScoped
public class UserRequestDistributedExecutor {

	@Inject
	@CacheName("distributed-executor-master-cache-node")
	private Cache<?, ?> masterCacheNode;
	
	@Inject
	private AsynchronousService localExecutorService;

	private DistributedExecutorService distributedExecutorService;
	
	@PostConstruct
	public void initialize(){
		distributedExecutorService = new DefaultExecutorService(masterCacheNode, localExecutorService);
		
        // ensure we're a topology aware cluster
        if ( ! (masterCacheNode.getAdvancedCache().getRpcManager().getAddress() instanceof TopologyAwareAddress))
        {
            throw new RuntimeException("must be in a topology aware cluster");
        }
	}
	
	
	public <T> Future<T> submit(Callable<T> task, UserKey input){
		return distributedExecutorService.submit(task, input);
	}
	
    public TopologyAwareAddress getLocalAddress(){
        return (TopologyAwareAddress) masterCacheNode.getAdvancedCache().getRpcManager().getAddress();
    }
    
    public TopologyAwareAddress getAddress(String machineId, String rackId, String siteId){
        List<Address> members = masterCacheNode.getAdvancedCache().getRpcManager().getMembers();
        for (Address member : members)
        {
            TopologyAwareAddress topologyAwareMember = (TopologyAwareAddress) member;
            if (topologyAwareMember.getMachineId().equals(machineId) &&
                topologyAwareMember.getRackId().equals(rackId) &&
                topologyAwareMember.getSiteId().equals(siteId)){
                return topologyAwareMember;
            }
        }
        return null;
    }
    
    public <T> Future<T> submit(TopologyAwareAddress target, Callable<T> task){
        return distributedExecutorService.submit(target, task);
    }
	
}
