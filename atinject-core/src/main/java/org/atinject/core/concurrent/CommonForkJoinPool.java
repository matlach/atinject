package org.atinject.core.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommonForkJoinPool implements ExecutorService{

    
    private ForkJoinPool forkJoinPool;
    
    @PostConstruct
    public void initialize() {
        // TODO this can be replaced by jdk8 ForkJoinPool.commonPool(); once it become available
        forkJoinPool = new ForkJoinPool();
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

    public <T> T invoke(ForkJoinTask<T> task) {
        return forkJoinPool.invoke(task);
    }

    public void execute(ForkJoinTask<?> task) {
        forkJoinPool.execute(task);
    }

    public <T> ForkJoinTask<T> submit(ForkJoinTask<T> task) {
        return forkJoinPool.submit(task);
    }

    public int getParallelism() {
        return forkJoinPool.getParallelism();
    }

    public int getPoolSize() {
        return forkJoinPool.getPoolSize();
    }

    public int getRunningThreadCount() {
        return forkJoinPool.getRunningThreadCount();
    }

    public int getActiveThreadCount() {
        return forkJoinPool.getActiveThreadCount();
    }

    public boolean isQuiescent() {
        return forkJoinPool.isQuiescent();
    }

    public long getStealCount() {
        return forkJoinPool.getStealCount();
    }

    public long getQueuedTaskCount() {
        return forkJoinPool.getQueuedTaskCount();
    }

    public int getQueuedSubmissionCount() {
        return forkJoinPool.getQueuedSubmissionCount();
    }

    public boolean hasQueuedSubmissions() {
        return forkJoinPool.hasQueuedSubmissions();
    }

    public boolean isTerminating() {
        return forkJoinPool.isTerminating();
    }

    @Override
    public void execute(Runnable command) {
        throw new RuntimeException("not implemented, use instead asynchronous service");
    }

    @Override
    public void shutdown() {
        forkJoinPool.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return forkJoinPool.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return forkJoinPool.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return forkJoinPool.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return forkJoinPool.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        throw new RuntimeException("not implemented, use instead asynchronous service");
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        throw new RuntimeException("not implemented, use instead asynchronous service");
    }

    @Override
    public Future<?> submit(Runnable task) {
        throw new RuntimeException("not implemented, use instead asynchronous service");
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throw new RuntimeException("not implemented, use instead asynchronous service");
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException {
        throw new RuntimeException("not implemented, use instead asynchronous service");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        throw new RuntimeException("not implemented, use instead asynchronous service");
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        throw new RuntimeException("not implemented, use instead asynchronous service");
    }
    
    
}
