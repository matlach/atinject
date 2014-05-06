package org.atinject.core.timer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
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
public class TimerService extends Service
{
    
    private static final int[] DEFAULT_SECONDS = {0};
    private static final int MIN_SECOND = 0;
    private static final int MAX_SECOND = 59; 
    
    private static final int[] DEFAULT_MINUTES = {0};
    private static final int MIN_MINUTE = 0;
    private static final int MAX_MINUTE = 59;
    
    private static final int[] DEFAULT_HOURS = {0};
    private static final int MIN_HOUR = 0;
    private static final int MAX_HOUR = 23;

    private static final int[] DEFAULT_DAYS_OF_WEEK = {-1};
    private static final int MIN_DAY_OF_WEEK = Calendar.SUNDAY;
    private static final int MAX_DAY_OF_WEEK = Calendar.SATURDAY;
    
    private static final int[] DEFAULT_DAYS_OF_MONTH = {-1};
    private static final int MIN_DAY_OF_MONTH = 1;
    // maxDayOfMonth varies by month
    
    private static final int[] DEFAULT_MONTHS = {-1};
    private static final int MIN_MONTH = Calendar.JANUARY;
    private static final int MAX_MONTH = Calendar.DECEMBER;

    private static final int DEFAULT_YEARS = -1;
    
    @Inject
    private Logger logger;
    
    @Inject ScheduledService scheduledService;
    
    @Inject TimerExtension timerExtension;
    
    @PostConstruct
    public void initialize() {
        for (final Method method : timerExtension.getScheduleAnnotatedMethods()) {
            Schedule schedule = method.getAnnotation(Schedule.class);
            if (schedule.clustered()){
                // skip clustered
                continue;
            }
            
            final Object object = CDI.select(method.getDeclaringClass()).get();
            
            final Timer timer = new Timer(schedule.seconds(), schedule.minutes(), schedule.hours(),
                    schedule.daysOfWeek(), schedule.daysOfMonth(), schedule.months(), schedule.years(), schedule.info(), schedule.timezone());
            updateTimer(timer);
            checkTimer(timer);
            
            scheduledService.schedule(new Runnable(){
                @Override
                public void run() {
                    try {
                        method.invoke(object);
                        
                        updateTimer(timer);
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
    
    public void schedule(int[] seconds, int[] minutes, int[] hours,
            int[] daysOfWeek, int[] daysOfMonth, int[] months, int years) {
        
//        if (seconds == null){
//            seconds = DEFAULT_SECONDS;
//        }
//        if (minutes == null){
//            minutes = DEFAULT_MINUTES;
//        }
//        if (hours == null){
//            hours = DEFAULT_HOURS;
//        }
//        if (daysOfWeek == null){
//            daysOfWeek = DEFAULT_DAYS_OF_WEEK;
//        }
//        if (daysOfMonth == null){
//            daysOfMonth = DEFAULT_DAYS_OF_MONTH;
//        }
//        if (months == null){
//            months = DEFAULT_MONTHS;
//        }
//        if (years == 0) {
//            years = DEFAULT_YEARS;
//        }
        
        // TODO

    }
    
    /**
     * Checks that alarm is not in the past
     * @exception RuntimeException if the alarm date is in the past
     */
    public void checkTimer(Timer timer) {
        long delay = timer.alarmTime - System.currentTimeMillis();
        
        if (delay < 0) {
            logger.debug("delay is negative : {}", delay);
            throw new RuntimeException("occuring in the past");
        }
    }
    
    /**
     * Updates this alarm entry to the next valid alarm time, AFTER the current time.
     */
    public void updateTimer(Timer timer){
        
        Calendar now = Calendar.getInstance();
        logger.debug("now: {}", now.getTime());
        
        
        Calendar next = (Calendar)now.clone();
        next.set( Calendar.MILLISECOND, 0 );
        // we assume this second is screwed, add 1 second
        next.add(Calendar.SECOND, 1);
        
        //
        // the updates work in a cascade -- if next minute value is in the
        // following hour, hour is incremented.  If next valid hour value is
        // in the following day, day is incremented, and so on.
        //
        int current = 0;
        int offset = 0;
        // increase alarm seconds
        proccessSeconds(next, timer);
        
        // increase alarm minutes
        current = next.get( Calendar.MINUTE );
        
        // force increment at least to next minute
        offset = getOffsetToNextOrEqual( current, MIN_MINUTE, MAX_MINUTE, timer.getMinutes() );
        next.add( Calendar.MINUTE, offset );
        logger.debug( "after min: {}", next.getTime() );
        
        // update alarm hours if necessary
        current = next.get( Calendar.HOUR_OF_DAY );  // (as updated by minute shift)
        offset = getOffsetToNextOrEqual( current, MIN_HOUR, MAX_HOUR, timer.getHours() );
        next.add( Calendar.HOUR_OF_DAY, offset );
        logger.debug( "after hour (current: {}): {}",current, next.getTime() );
        
        //
        // If days of month AND days of week are restricted, we take whichever match
        // comes sooner.
        // If only one is restricted, take the first match for that one.
        // If neither is restricted, don't do anything.
        //
        if( timer.getDaysOfMonth()[0] != -1 && timer.getDaysOfWeek()[0] != -1 )
        {
            // BOTH are restricted - take earlier match
            Calendar dayOfWeekAlarm = (Calendar)next.clone();
            updateDayOfWeekAndMonth(timer, dayOfWeekAlarm );
            
            Calendar dayOfMonthAlarm = (Calendar)next.clone();
            updateDayOfMonthAndMonth(timer, dayOfMonthAlarm );
            
            // take the earlier one
            if( dayOfMonthAlarm.getTime().getTime() < dayOfWeekAlarm.getTime().getTime() )
            {
                next = dayOfMonthAlarm;
                logger.debug( "after dayOfMonth CLOSER: {}", next.getTime() );
            }
            else
            {
                next = dayOfWeekAlarm;
                logger.debug( "after dayOfWeek CLOSER: {}", next.getTime() );
            }
        }
        else if( timer.getDaysOfWeek()[0] != -1 ) // only dayOfWeek is restricted
        {
            // update dayInWeek and month if necessary
            updateDayOfWeekAndMonth(timer, next );
            logger.debug( "after dayOfWeek: {}", next.getTime() );
        }
        else if( timer.getDaysOfMonth()[0] != -1 ) // only dayOfMonth is restricted
        {
            // update dayInMonth and month if necessary
            updateDayOfMonthAndMonth(timer, next );
            logger.debug( "after dayOfMonth: {}", next.getTime() );
        }
        // else if neither is restricted (both[0] == -1), we don't need to do anything.
        
        logger.debug("alarm: {}", next.getTime());
        
        timer.alarmTime = next.getTime().getTime();
    }
    
    /**
     * daysInMonth can't use simple offsets like the other fields, because the
     * number of days varies per month (think of an alarm that executes on every
     * 31st).  Instead we advance month and dayInMonth together until we're on a
     * matching value pair.
     */
    private void updateDayOfMonthAndMonth(Timer timer, Calendar next)
    {
        int currentMonth = next.get( Calendar.MONTH );
        int currentDayOfMonth = next.get( Calendar.DAY_OF_MONTH );
        int offset = 0;
        
        // loop until we have a valid day AND month (if current is invalid)
        while( !isIn(currentMonth, timer.getMonths()) || !isIn(currentDayOfMonth, timer.getDaysOfMonth()) )
        {
            // if current month is invalid, advance to 1st day of next valid month
            if( !isIn(currentMonth, timer.getMonths()) )
            {
                offset = getOffsetToNextOrEqual( currentMonth, MIN_MONTH, MAX_MONTH, timer.getMonths() );
                next.add( Calendar.MONTH, offset );
                next.set( Calendar.DAY_OF_MONTH, 1 );
                currentDayOfMonth = 1;
            }
            
            // advance to the next valid day of month, if necessary
            if( !isIn(currentDayOfMonth, timer.getDaysOfMonth()) )
            {
                int maxDayOfMonth = next.getActualMaximum( Calendar.DAY_OF_MONTH );
                offset = getOffsetToNextOrEqual( currentDayOfMonth, MIN_DAY_OF_MONTH, maxDayOfMonth, timer.getDaysOfMonth() );
                next.add( Calendar.DAY_OF_MONTH, offset );
            }
            
            currentMonth = next.get( Calendar.MONTH );
            currentDayOfMonth = next.get( Calendar.DAY_OF_MONTH );
        }
    }
    
    
    private void updateDayOfWeekAndMonth(Timer timer, Calendar next)
    {
        int currentMonth = next.get( Calendar.MONTH );
        int currentDayOfWeek = next.get( Calendar.DAY_OF_WEEK );
        int offset = 0;
        
        // loop until we have a valid day AND month (if current is invalid)
        while( !isIn(currentMonth, timer.getMonths()) || !isIn(currentDayOfWeek, timer.getDaysOfWeek()) )
        {
            // if current month is invalid, advance to 1st day of next valid month
            if( !isIn(currentMonth, timer.getMonths()) )
            {
                offset = getOffsetToNextOrEqual( currentMonth, MIN_MONTH, MAX_MONTH, timer.getMonths() );
                next.add( Calendar.MONTH, offset );
                next.set( Calendar.DAY_OF_MONTH, 1 );
                currentDayOfWeek = next.get( Calendar.DAY_OF_WEEK );
            }
            
            // advance to the next valid day of week, if necessary
            if( !isIn(currentDayOfWeek, timer.getDaysOfWeek()) )
            {
                offset = getOffsetToNextOrEqual( currentDayOfWeek, MIN_DAY_OF_WEEK, MAX_DAY_OF_WEEK, timer.getDaysOfWeek() );
                next.add( Calendar.DAY_OF_YEAR, offset );
            }
            
            currentDayOfWeek = next.get( Calendar.DAY_OF_WEEK );
            currentMonth = next.get( Calendar.MONTH );
        }
    }
    
    private Calendar proccessSeconds(Calendar now, Timer timer) {
        int offset = getOffsetToNextSecond( now, timer );
        now.add(Calendar.SECOND, offset);
        logger.debug( "after sec: {}", now.getTime() );
        return now;
    }    
    
    // ----------------------------------------------------------------------
    //                      General utility methods
    // ----------------------------------------------------------------------
    
    private int getOffsetToNextSecond(Calendar now, Timer timer) {
    	if (timer.isValidForEachSeconds()) {
    		return 0;
    	}
    	return getOffsetToNext(now.get(Calendar.SECOND), MIN_SECOND, MAX_SECOND, timer.getSeconds());
    }

    /**
     * if values = {-1} or current is valid
     *   offset is 0.
     * if current < last(values)
     *   offset is diff to next valid value
     * if current >= last(values)
     *   offset is diff to values[0], wrapping from max to min
     */
    private static int getOffsetToNextOrEqual( int current, int min, int max, int[] values ) {
        // find the distance to the closest valid value >= current (wrapping if necessary)
        
        // offset is 0 if current is valid value
        if (isIn(current, values) ) {
            return 0;
        }
        
        return getOffsetToNext(current, min, max, values);
    }
    
    /**
     * if current < last(values)
     *   offset is diff to next valid value
     * if current >= last(values)
     *   offset is diff to values[0], wrapping from max to min
     */
    private static int getOffsetToNext( int current, int min, int max, int[] values ) {
        // need to wrap
        if( current >= last(values) ){
            int next = first(values);
            return (max - current + 1) + (next - min);
        }
        
        // current < max(values) -- find next valid value after current
        for( int i=0; i<values.length; i++ ) {
            if( current < values[i] ) {
                return values[i] - current;
            }
        }
        throw new RuntimeException("assert, array not sorted");
    }
    
    /**
     * handles -1 in values as * and returns true
     * otherwise returns true iff given value is in the array
     */
    private static boolean isIn( int find, int[] values ) {
    	// TODO should not be checked here
        if( values[0] == -1 )
        {
            return true;
        }
        return Arrays.binarySearch(values, find) >= 0;
    }
    
    private static int first(int[] intArray) {
    	return intArray[0];
    }
    
    private static int last( int[] intArray ) {
        return intArray[ intArray.length - 1 ];
    }
        
}
