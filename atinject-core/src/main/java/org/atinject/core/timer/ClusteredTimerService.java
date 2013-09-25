package org.atinject.core.timer;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.atinject.core.cdi.CDI;
import org.atinject.core.concurrent.ScheduledService;
import org.atinject.core.startup.Startup;
import org.atinject.core.tiers.Service;
import org.slf4j.Logger;

@Startup
@ApplicationScoped
public class ClusteredTimerService extends Service {

    @Inject
    Logger logger;
    
//    @Inject
//    Cache cache;
    
    @Inject
    TimerExtension timerExtension;
    
    @Inject
    TimerService timerService;
    
    @Inject
    ScheduledService scheduledService;
    
    @PostConstruct
    public void initialize() {
        for (final Method method : timerExtension.getScheduleAnnotatedMethods()) {
            Schedule schedule = method.getAnnotation(Schedule.class);
            if (! schedule.clustered()){
                // skip clustered
                continue;
            }
            
            final Object object = CDI.select(method.getDeclaringClass()).get();
            
            final Timer timer = new Timer(schedule.seconds(), schedule.minutes(), schedule.hours(),
                    schedule.daysOfWeek(), schedule.daysOfMonth(), schedule.months(), schedule.years());
            timerService.updateTimer(timer);
            timerService.checkTimer(timer);
            
            scheduledService.schedule(new Runnable(){
                @Override
                public void run() {
                    try {
                        method.invoke(object);
                        
                        timerService.updateTimer(timer);
                        long nextAlarm = timer.alarmTime - System.currentTimeMillis();
                        logger.info("next alarm in : {}", TimeUnit.MILLISECONDS.toSeconds(nextAlarm));
                        scheduledService.schedule(this, nextAlarm, TimeUnit.MILLISECONDS);
                    }
                    catch (Exception e) {
                        // swallow
                        logger.error("error while invoking scheduled", e);
                    }
                }}, 0, TimeUnit.SECONDS);
        }
    }
}
