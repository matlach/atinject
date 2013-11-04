package org.atinject.core.distevent;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.atinject.core.cache.DistributedCache;
import org.atinject.core.cdi.CDI;
import org.atinject.core.cdi.Named;
import org.atinject.core.concurrent.AsynchronousService;
import org.atinject.core.event.Event;
import org.atinject.core.startup.Startup;
import org.infinispan.distexec.DefaultExecutorService;

@Startup
@ApplicationScoped
public class DistributedEventBus
{
    @Inject @Named("distributed-executor") private DistributedCache<Object, Object> masterCacheNode;
    
    @Inject
    private AsynchronousService localExecutorService;

    private DefaultExecutorService distributedExecutorService;

    @PostConstruct
    public void initialize() {
        distributedExecutorService = new DefaultExecutorService(masterCacheNode.unwrap(), localExecutorService);
    }
    
    public List<Future<Void>> onDistributedEventFired(@Observes(during=TransactionPhase.AFTER_SUCCESS) @Distributed final Event event) {
        DistributedEventTask distributedEventTask = new DistributedEventTask().setEvent(event);
        return distributedExecutorService.submitEverywhere(distributedEventTask);
    }
    
    public static class DistributedEventTask implements Callable<Void>, Serializable {
        private static final long serialVersionUID = 1L;

        private Event event;
        
        public DistributedEventTask setEvent(Event event) {
            this.event = event;
            return this;
        }
        
        @Override
        public Void call() throws Exception {
            CDI.getBeanManager().fireEvent(event);
            return null;
        }
        
    }
}
