package org.atinject.core.profiling;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;

import org.atinject.core.concurrent.Asynchronous;
import org.atinject.core.concurrent.ScheduledService;
import org.slf4j.Logger;

@ApplicationScoped
public class ProfilingServiceImpl implements ProfilingService
{

    @Inject
    private Logger logger;
    
    @Inject
    private ScheduledService schedulingService;
    
    private ConcurrentMap<Method, Long[]> statistics;
    private ConcurrentSkipListSet<Method> dirtyKeys;
    
    @PostConstruct
    public void initialize()
    {
        statistics = new ConcurrentHashMap<>();
        
        // ms until the next minute = ...
        // schedule each minute
        //schedulingService.scheduleAtFixedRate(command, initialDelay, period, unit);
        
        // ms until the next hour = ...
        // schedule each hour
    }
    
    @Asynchronous
    @Override
    public void addProfiling(InvocationContext invocationContext, long t)
    {
        logger.info("method '{}' took {}ms", invocationContext.getMethod().getName(), t / 1000000);
    }

    private void eachMinute()
    {
        
    }
    
    private void eachHour()
    {
        
    }
    
    public void reset()
    {
        
    }
}
