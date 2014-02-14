package org.atinject.core.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ScheduledService implements ScheduledExecutorService {

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    
    @PostConstruct
    public void initialize() {
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory());
    }
    
    /**
     * @see Executors#defaultThreadFactory()
     */
    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                                  Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                          poolNumber.getAndIncrement() +
                         "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                                  namePrefix + threadNumber.getAndIncrement(),
                                  0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    @PreDestroy
    public void cleapUp() {
        shutdown();
        try {
            awaitTermination(30L, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void getActiveCount() {
        scheduledThreadPoolExecutor.getActiveCount();
    }
    
    public void getCompletedTaskCount() {
        scheduledThreadPoolExecutor.getCompletedTaskCount();
    }
    
    public void getCorePoolSize() {
        scheduledThreadPoolExecutor.getCorePoolSize();
    }
    
    public void getLargestPoolSize() {
        scheduledThreadPoolExecutor.getLargestPoolSize();
    }
    
    public void getMaximumPoolSize() {
        scheduledThreadPoolExecutor.getMaximumPoolSize();
    }
    
    public void getPoolSize() {
        scheduledThreadPoolExecutor.getPoolSize();
    }
    
    public void getQueueSize() {
        scheduledThreadPoolExecutor.getQueue().size();
    }
    
    public void getTaskCount() {
        scheduledThreadPoolExecutor.getTaskCount();
    }
    
    @Override
    public void shutdown() {
        scheduledThreadPoolExecutor.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return scheduledThreadPoolExecutor.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return scheduledThreadPoolExecutor.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return scheduledThreadPoolExecutor.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return scheduledThreadPoolExecutor.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        throw new RuntimeException("not implemented, use asynchronous service");
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        throw new RuntimeException("not implemented, use asynchronous service");
    }

    @Override
    public Future<?> submit(Runnable task) {
        throw new RuntimeException("not implemented, use asynchronous service");
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throw new RuntimeException("not implemented, use asynchronous service");
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        throw new RuntimeException("not implemented, use asynchronous service");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        throw new RuntimeException("not implemented, use asynchronous service");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new RuntimeException("not implemented, use asynchronous service");
    }

    @Override
    public void execute(Runnable command) {
        throw new RuntimeException("not implemented, use asynchronous service");
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return scheduledThreadPoolExecutor.schedule(command, delay, unit);
    }

    @Override
    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return scheduledThreadPoolExecutor.schedule(callable, delay, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return scheduledThreadPoolExecutor.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return scheduledThreadPoolExecutor.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }
}
