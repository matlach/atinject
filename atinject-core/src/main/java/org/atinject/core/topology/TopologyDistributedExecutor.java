package org.atinject.core.topology;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.Named;
import org.atinject.core.concurrent.AsynchronousService;
import org.infinispan.distexec.DefaultExecutorService;
import org.infinispan.distexec.DistributedExecutorService;
import org.infinispan.remoting.transport.TopologyAwareAddress;

@ApplicationScoped
public class TopologyDistributedExecutor
{
    @Inject @Named("distributed-executor") private DistributedCache<Object, Object> masterCacheNode;
    
    @Inject
    private AsynchronousService localExecutorService;

    @Inject
    private TopologyService topologyService;
    
    private DistributedExecutorService distributedExecutorService;
    
    @PostConstruct
    public void initialize(){
        distributedExecutorService = new DefaultExecutorService(masterCacheNode.unwrap(), localExecutorService);
    }
    
    public <T> Future<T> submit(String machineId, Callable<T> task){
        TopologyAwareAddress target = topologyService.getAddress(machineId);
        if (target == null){
            // member has left the cluster, no-op
            return null; // return null or throw exception ?
        }
        return distributedExecutorService.submit(target, task);
    }
    
    // TODO submit all, etc.
}
