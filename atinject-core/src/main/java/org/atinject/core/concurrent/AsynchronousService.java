package org.atinject.core.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.atinject.core.cdi.CDI;

@ApplicationScoped
public class AsynchronousService extends ManagedExecutorService {

    /**
     * @see Executors#newFixedThreadPool(int)
     * @see ThreadPoolExecutor#allowCoreThreadTimeOut(boolean)
     */
    @PostConstruct
    public void initialize() {
        threadPoolExecutor = new ThreadPoolExecutor(100, 100,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ContextAwareThreadFactory(),
                new DefaultRejectedExecutionHandler());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
    }
    
    public class TaskExecutionRejected {
    	Runnable rejectedTask;
    	ManagedExecutorService executorService;
    }
    
    public class DefaultRejectedExecutionHandler implements RejectedExecutionHandler {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			CDI.getBeanManager().fireEvent(new TaskExecutionRejected());
		}
    	
    }
    
    @PreDestroy
    public void cleanUp() {
        shutdown();
        try {
            awaitTermination(60L, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
