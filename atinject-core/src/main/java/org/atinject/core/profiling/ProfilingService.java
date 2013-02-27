package org.atinject.core.profiling;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;

import org.atinject.core.concurrent.Asynchronous;
import org.atinject.core.concurrent.ScheduledService;
import org.slf4j.Logger;

@ApplicationScoped
public class ProfilingService
{

    @Inject
    private Logger logger;
    
    @Inject
    private ScheduledService schedulingService;
    
    private ConcurrentMap<Method, CurrentStatistics> currentStatistics;
    private ConcurrentMap<Method, CompressedStatisticsRoundRobinArray> lastThreeHoursMinutelySmoothed;
    // TODO consider adding more compressed statistics... last 8 hours, last day, last week, etc.
    // to minimize memory consumption, the more we'll advance in time, the less it will be precise (hourly smoothed, daily smoothed, etc.)
    
    private static class CompressedStatisticsRoundRobinArray{
        CompressedStatistics[] elements;
        int index;
        int size;
        
        public CompressedStatisticsRoundRobinArray(int capacity){
            elements = new CompressedStatistics[capacity];
        }
        
        public void add(CompressedStatistics e){
            synchronized (this){
                elements[index] = e;
                index = index + 1;
                if (size < elements.length){
                    size = size + 1;
                }
                if (index == elements.length){
                    index = 0;
                }
            }
        }
    }
    
    // TODO use long[] array that will grow if needed instead of relying on arraylist, that way we can eliminate boxing conversion between Long and long
    private static class CurrentStatistics{
        private List<Long> invocations = new ArrayList<>();
        
        public void add(long t){
            synchronized(this){
                // TODO consider adding a max size to the sampling to prevent OOME
                invocations.add(t);
            }
        }
        
        public CompressedStatistics compress(){
            synchronized(this){
                // we should not compress if there was no invocations
                long sum = 0;
                long min = Long.MAX_VALUE;
                long max = Long.MIN_VALUE;
                for (Long t : invocations){
                    min = Math.min(min, t);
                    max = Math.max(max, t);
                    sum = sum + t;
                }
                int count = invocations.size();
                long mean = sum / (count + 1);
                invocations.clear();
                
                CompressedStatistics compressedStatistics = new CompressedStatistics();
                compressedStatistics.count = count; compressedStatistics.mean = mean;
                compressedStatistics.min = min; compressedStatistics.max = max;
                
                return compressedStatistics;
            }
        }
    }
    
    private static class CompressedStatistics{
        int count;
        long mean;
        long min;
        long max;
    }
    
    @PostConstruct
    public void initialize()
    {
        currentStatistics = new ConcurrentHashMap<>();
        lastThreeHoursMinutelySmoothed = new ConcurrentHashMap<>();
        long millisUntilNextMinute = 60 * 1000; // TODO to calculate
        // schedule each minute
        schedulingService.scheduleAtFixedRate(new Runnable(){
            @Override
            public void run(){
                eachMinute();
            }
            
        }, millisUntilNextMinute, 60 * 1000, TimeUnit.MILLISECONDS);
    }
    
    @Asynchronous
    public void addProfiling(InvocationContext invocationContext, long t)
    {
        CurrentStatistics statistics = currentStatistics.get(invocationContext.getMethod());
        if (statistics == null){
            CurrentStatistics newStatistics = new CurrentStatistics();
            statistics = currentStatistics.putIfAbsent(invocationContext.getMethod(), newStatistics);
            if (statistics == null){
                statistics = newStatistics;
            }
        }
        statistics.add(t);
        logger.info("method '{}' took {}ms", invocationContext.getMethod().getName(), t / 1000000);
    }

    private void eachMinute()
    {
        // compress data for each method
        for (Entry<Method, CurrentStatistics> entry : currentStatistics.entrySet()){
            CompressedStatisticsRoundRobinArray statistics = lastThreeHoursMinutelySmoothed.get(entry.getKey());
            if (statistics == null){
                CompressedStatisticsRoundRobinArray newStatistics = new CompressedStatisticsRoundRobinArray(3 * 60); // 3 x 60 minutes = 3 hours
                statistics = lastThreeHoursMinutelySmoothed.putIfAbsent(entry.getKey(), newStatistics);
                if (statistics == null){
                    statistics = newStatistics;
                }
            }
            if (entry.getValue().invocations.isEmpty()){
                statistics.add(null); // no significant value, add blank
            }
            else{
                CompressedStatistics compressedStatistics = entry.getValue().compress();
                statistics.add(compressedStatistics);
                System.out.println(entry.getKey() + " count " + compressedStatistics.count + " mean " + compressedStatistics.mean);
            }
        }
    }
    
    public void reset()
    {
        currentStatistics.clear();
        lastThreeHoursMinutelySmoothed.clear();
    }
    
    // most invoked
    
    // slowest
    
    // trend, if average time is increasing or lowering time over time of last X time
}
