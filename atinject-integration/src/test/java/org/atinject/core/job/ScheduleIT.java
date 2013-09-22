package org.atinject.core.job;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.atinject.core.concurrent.ScheduledService;
import org.atinject.integration.IntegrationTest;
import org.junit.Test;
import org.slf4j.Logger;

public class ScheduleIT extends IntegrationTest {

    @Inject ScheduledService scheduledService;
    
    @Inject Logger logger;
    
    @Test
    public void testScheduleAt() throws Exception{
        int[] seconds = new int[]{-1};
        int[] minutes = new int[]{-1};
        int[] hours = new int[]{-1};
        int[] daysOfWeek = new int[]{-1};
        int[] daysOfMonth = new int[]{-1};
        int[] months = new int[]{-1};
        int years = -1;
        
        final AlarmEntry alarm = new AlarmEntry(seconds, minutes, hours, daysOfWeek, daysOfMonth, months, years);
        
        scheduledService.schedule(new Runnable(){
            @Override
            public void run() {
                alarm.updateAlarmTime();
                long nextAlarm = alarm.alarmTime - System.currentTimeMillis();
                logger.info("next alarm in : {}", TimeUnit.MILLISECONDS.toSeconds(nextAlarm));
                scheduledService.schedule(this, nextAlarm, TimeUnit.MILLISECONDS);
            }}, 0, TimeUnit.SECONDS);
        
        Thread.sleep(Integer.MAX_VALUE);
    }

}
