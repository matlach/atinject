package org.atinject.core.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class ManagedExecutorService implements ExecutorService {

	protected ThreadPoolExecutor threadPoolExecutor;
	
    public void getActiveCount() {
        threadPoolExecutor.getActiveCount();
    }
    
    public void getCompletedTaskCount() {
        threadPoolExecutor.getCompletedTaskCount();
    }
    
    public void getCorePoolSize() {
        threadPoolExecutor.getCorePoolSize();
    }
    
    public void getLargestPoolSize() {
        threadPoolExecutor.getLargestPoolSize();
    }
    
    public void getMaximumPoolSize() {
        threadPoolExecutor.getMaximumPoolSize();
    }
    
    public void getPoolSize() {
        threadPoolExecutor.getPoolSize();
    }
    
    public void getQueueSize() {
        threadPoolExecutor.getQueue().size();
    }
    
    public void getTaskCount() {
        threadPoolExecutor.getTaskCount();
    }
    
    @Override
    public void execute(Runnable command) {
        threadPoolExecutor.execute(command);
    }

    @Override
    public void shutdown() {
        threadPoolExecutor.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return threadPoolExecutor.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return threadPoolExecutor.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return threadPoolExecutor.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return threadPoolExecutor.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return threadPoolExecutor.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return threadPoolExecutor.submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return threadPoolExecutor.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return threadPoolExecutor.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return threadPoolExecutor.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return threadPoolExecutor.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return threadPoolExecutor.invokeAny(tasks, timeout, unit);
    }
}
