package org.atinject.core.job;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.concurrent.ScheduledService;
import org.slf4j.Logger;

@ApplicationScoped
public class JobSchedulerService {

    @Inject private Logger logger;
    
    @Inject private JobCacheStore jobCacheStore;
    
    @Inject private ScheduledService scheduledService;
    
    @PostConstruct
    public void initialize() {
        // schedule locally all job currently in cache / store ?
    }
    
    public void scheduleJob(String jobName){
        
        if (jobCacheStore.getJob(jobName) != null){
            throw new RuntimeException("job is already scheduled, re schedule job instead");
        }
        
        Job job = new Job()
            .setName(jobName);
        
        JobWrapper wrappedJob = new JobWrapper(job);
        long initialDelay = 0;
        long period = 0;
        
        ScheduledFuture<Void> future = (ScheduledFuture<Void>) scheduledService.scheduleAtFixedRate(wrappedJob, initialDelay, period, TimeUnit.MILLISECONDS);
        
        jobCacheStore.putScheduledFuture(jobName, future);
    }
    
    public void unscheduleJob(String jobName) {
        Job job = jobCacheStore.getJob(jobName);
        if (job == null){
            throw new RuntimeException("");
        }
        unscheduleJob(job);
    }
    
    public void unscheduleJob(Job job){
        jobCacheStore.removeJob(job);
        ScheduledFuture<Void> future = jobCacheStore.getScheduledFuture(job.getName());
        if (future != null){
            future.cancel(true);
        }
    }
    
    public void rescheduleJob(String jobName) {
        unscheduleJob(jobName);
        scheduleJob(jobName);
    }
    
    // job wrapper
    
    private class JobWrapper implements Runnable {
    
        private Job job;
        
        public JobWrapper(Job job){
            this.job = job;
        }
        
        @Override
        public void run(){
            // begin transaction
            try {
                if (! jobCacheStore.getLock(job.getName())){
                    // another node is already processing job,
                    // skip invocation
                    
                    logger.info("job invocation skipped, scheduled next: ");
                    return;
                }
                
                job.run();
                logger.info("job invocation finished, scheduled next: ");
            }
            catch (Exception e){
                
            }
            finally {
                
            }
            // commit transaction
        }
    }
}
