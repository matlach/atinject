package org.atinject.core.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IoAsynchronousService extends ManagedExecutorService {

    /**
     * @see Executors#newFixedThreadPool(int)
     * @see ThreadPoolExecutor#allowCoreThreadTimeOut(boolean)
     */
    @PostConstruct
    public void initialize() {
        threadPoolExecutor = new ThreadPoolExecutor(100, 100,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ContextAwareThreadFactory());
        // TODO implements RejectedExecutionHandler
        threadPoolExecutor.allowCoreThreadTimeOut(true);
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
