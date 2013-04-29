package org.atinject.core.job;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.atinject.core.cache.ClusteredCache;
import org.infinispan.Cache;
import org.infinispan.context.Flag;

@ApplicationScoped
public class JobCacheStore {

    // local cache
    private Map<String, ScheduledFuture<Void>> scheduledFutures;
    
    private ClusteredCache<String, Job> jobs;
    
    private Cache<String, Object> locks;
    
    @PostConstruct
    public void initialize(){
        scheduledFutures = new HashMap<>();
    }
    
    public Job getJob(String jobName){
        return jobs.get(jobName);
    }
    
    public void putJob(Job job){
        jobs.put(job.getName(), job);
    }
    
    public void removeJob(Job job){
        jobs.remove(job.getName());
    }
    
    public ScheduledFuture<Void> getScheduledFuture(String jobName){
        return scheduledFutures.get(jobName);
    }
    
    public void putScheduledFuture(String jobName, ScheduledFuture<Void> scheduledFuture){
        scheduledFutures.put(jobName, scheduledFuture);
    }
    
    public void removeScheduledFuture(String jobName){
        scheduledFutures.remove(jobName);
    }
    
    public boolean getLock(String jobName){
        return locks.getAdvancedCache().withFlags(Flag.ZERO_LOCK_ACQUISITION_TIMEOUT, Flag.FAIL_SILENTLY).lock(jobName);
    }
    
}
