package org.atinject.core.distevent;

import java.io.Serializable;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.atinject.core.cache.CacheName;
import org.atinject.core.cdi.BeanManagerExtension;
import org.atinject.core.concurrent.AsynchronousService;
import org.atinject.core.event.BaseEvent;
import org.atinject.core.startup.Startup;
import org.infinispan.Cache;
import org.infinispan.distexec.DefaultExecutorService;

@Startup
@ApplicationScoped
public class DistributedEventBus
{
    @Inject
    @CacheName("distributed-executor-master-cache-node")
    private Cache<?, ?> masterCacheNode;
    
    @Inject
    private AsynchronousService localExecutorService;

    private DefaultExecutorService distributedExecutorService;

    @PostConstruct
    public void initialize(){
        distributedExecutorService = new DefaultExecutorService(masterCacheNode, localExecutorService);
    }
    
    public void onDistributedEventFired(@Observes(during=TransactionPhase.AFTER_SUCCESS) @Distributed final BaseEvent event)
    {
        DistributedEventTask distributedEventTask = new DistributedEventTask();
        distributedEventTask.setEvent(event);
        distributedExecutorService.submitEverywhere(distributedEventTask);
    }
    
    public static class DistributedEventTask implements Callable<Void>, Serializable
    {
        private static final long serialVersionUID = 1L;

        private BaseEvent event;
        
        public DistributedEventTask()
        {
            
        }
        
        public void setEvent(BaseEvent event)
        {
            this.event = event;
        }
        
        @Override
        public Void call() throws Exception
        {
            BeanManagerExtension.fireEvent(event);
            return null;
        }
        
    }
}
