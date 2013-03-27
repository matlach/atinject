package org.atinject.core.distexec;

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
import org.infinispan.remoting.transport.TopologyAwareAddress;

@ApplicationScoped
public class TopologyDistributedExecutor
{
    @Inject @CacheName("distributed-executor-master-cache-node") private Cache<Object, Object> masterCacheNode;
    
    @Inject
    private AsynchronousService localExecutorService;

    @Inject
    private TopologyService topologyService;
    
    private DistributedExecutorService distributedExecutorService;
    
    @PostConstruct
    public void initialize(){
        distributedExecutorService = new DefaultExecutorService(masterCacheNode, localExecutorService);
    }
    
    public <T> Future<T> submit(String machineId, Callable<T> task){
        TopologyAwareAddress target = topologyService.getAddress(machineId);
        if (target == null){
            // member has left the cluster, no-op
            return null; // return null or throw exception ?
        }
        return distributedExecutorService.submit(target, task);
    }
}
