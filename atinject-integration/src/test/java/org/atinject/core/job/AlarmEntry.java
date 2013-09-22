/*
 *  com/jtheory/jdring/AlarmEntry.java
 *  Copyright (C) 1999 - 2004 jtheory creations, Olivier Dedieu et al.
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Library General Public License as published
 *  by the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU Library General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */

package org.atinject.core.job;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AlarmEntry {
    private Logger logger = LoggerFactory.getLogger(AlarmEntry.class);
    
    private static final int[] DEFAULT_SECONDS = {-1};
    private static final int MIN_SECOND = 0;
    private static final int MAX_SECOND = 59; 
    
    private static final int[] DEFAULT_MINUTES = {-1};
    private static final int MIN_MINUTE = 0;
    private static final int MAX_MINUTE = 59;
    
    private static final int[] DEFAULT_HOURS = {-1};
    private static final int MIN_HOUR = 0;
    private static final int MAX_HOUR = 23;

    private static final int[] DEFAULT_DAYS_OF_WEEK = {-1};
    private static final int MIN_DAY_OF_WEEK = 1;
    private static final int MAX_DAY_OF_WEEK = 7;
    
    private static final int[] DEFAULT_DAYS_OF_MONTH = {-1};
    private static final int MIN_DAY_OF_MONTH = 1;
    // maxDayOfMonth varies by month
    
    private static final int[] DEFAULT_MONTHS = {-1};
    private static final int MIN_MONTH = 0;
    private static final int MAX_MONTH = 11;

    private static final int DEFAULT_YEARS = -1;
    
    private int[] seconds;
    private int[] minutes;
    private int[] hours;
    private int[] daysOfWeek;
    private int[] daysOfMonth;
    private int[] months;
    
    private int year; // no support for a list of years -- must be * or specified
    
    public long alarmTime;
    
    /**
     * <p>Creates a new AlarmEntry.  Extended cron format - supports lists
     * of values for each field, or {-1} to allow all values for that field.</p>
     *
     * <p>Params of (30, 13, -1, -1, 2, -1, listener) schedule an alarm for
     * 1:30pm every Monday.</p>
     *
     * <p>NOTE: if both dayOfMonth and dayOfWeek are restricted, each alarm will
     * be scheduled for the sooner match.</p>
     *
     * @param minutes valid minutes of the alarm. Allowed values
     * 0-59, or {-1} for all.
     * @param hours valid hours of the alarm. Allowed values 0-23,
     * or {-1} for all.
     * @param daysOfMonth valid days of month of the alarm.  Allowed
     * values 1-31, or {-1} for all.
     * @param months valid months of the alarm. Allowed values
     * 0-11 (0 = January, 1 = February, ...), or {-1} for all.
     * <code>java.util.Calendar</code> constants can be used.
     * @param daysOfWeek valid days of week of the alarm. This attribute
     * is exclusive with <code>dayOfMonth</code>. Allowed values 1-7
     * (1 = Sunday, 2 = Monday, ...), or {-1} for all.
     * <code>java.util.Calendar</code> constants can be used.
     * @param year year of the alarm. When this field is not set (i.e. -1)
     * the alarm is repetitive (i.e. it is rescheduled when reached).
     * @param listener the alarm listener.
     * @return the AlarmEntry.
     * @exception PastDateException if the alarm date is in the past
     * (or less than 1 second away from the current date).
     */
    public AlarmEntry(
            int[] _seconds,
            int[] _minutes,
            int[] _hours,
            int[] _daysOfWeek,
            int[] _daysOfMonth,
            int[] _months,
            int _year) {
        if (_seconds == null){
            seconds = DEFAULT_SECONDS;
        }
        else {
            seconds = _seconds;
        }
        
        if (_minutes == null){
            minutes = DEFAULT_MINUTES;
        }
        else{
            minutes = _minutes;
        }
        
        if (_hours == null){
            hours = DEFAULT_HOURS;
        }
        else{
            hours = _hours;
        }
        
        if (_daysOfWeek == null){
            daysOfWeek = DEFAULT_DAYS_OF_WEEK;
        }
        else {
            daysOfWeek = _daysOfWeek;
        }
        
        if (_daysOfMonth == null){
            daysOfMonth = DEFAULT_DAYS_OF_MONTH;
        }
        else {
            daysOfMonth = _daysOfMonth;
        }
        
        if (_months == null){
            months = DEFAULT_MONTHS;
        }
        else {
            months = _months;
        }

        if (_year == 0) {
            year = DEFAULT_YEARS;
        }
        else {
            year = _year;
        }
        
        updateAlarmTime();
        checkAlarmTime();
    }
    
    /**
     * Checks that alarm is not in the past, or less than 1 second
     * away.
     *
     * @exception PastDateException if the alarm date is in the past
     * (or less than 1 second in the future).
     */
    void checkAlarmTime() {
        long delay = alarmTime - System.currentTimeMillis();
        
        if (delay < 0) {
            logger.debug("delay is negative :" + delay);
            throw new RuntimeException("occuring in the past");
        }
    }
    
    /**
     * Updates this alarm entry to the next valid alarm time, AFTER the current time.
     */
    public void updateAlarmTime() {
        
        Calendar now = Calendar.getInstance();
        logger.debug("now: " + now.getTime());
        
        
        Calendar alarm = (Calendar)now.clone();
        alarm.set( Calendar.MILLISECOND, 0 );
        // we assume this second is screwed, add 1 second
        alarm.add(Calendar.SECOND, 1);
        
        //
        // the updates work in a cascade -- if next minute value is in the
        // following hour, hour is incremented.  If next valid hour value is
        // in the following day, day is incremented, and so on.
        //
        
        // increase alarm seconds
        int current = alarm.get( Calendar.SECOND );
        int offset = getOffsetToNext( current, MIN_SECOND, MAX_SECOND, seconds );
        logger.debug( "after sec: " + alarm.getTime() );
        
        // increase alarm minutes
        current = alarm.get( Calendar.MINUTE );
        
        // force increment at least to next minute
        offset = getOffsetToNextOrEqual( current, MIN_MINUTE, MAX_MINUTE, minutes );
        alarm.add( Calendar.MINUTE, offset );
        logger.debug( "after min: " + alarm.getTime() );
        
        // update alarm hours if necessary
        current = alarm.get( Calendar.HOUR_OF_DAY );  // (as updated by minute shift)
        offset = getOffsetToNextOrEqual( current, MIN_HOUR, MAX_HOUR, hours );
        alarm.add( Calendar.HOUR_OF_DAY, offset );
        logger.debug( "after hour (current:"+current+"): " + alarm.getTime() );
        
        //
        // If days of month AND days of week are restricted, we take whichever match
        // comes sooner.
        // If only one is restricted, take the first match for that one.
        // If neither is restricted, don't do anything.
        //
        if( daysOfMonth[0] != -1 && daysOfWeek[0] != -1 )
        {
            // BOTH are restricted - take earlier match
            Calendar dayOfWeekAlarm = (Calendar)alarm.clone();
            updateDayOfWeekAndMonth( dayOfWeekAlarm );
            
            Calendar dayOfMonthAlarm = (Calendar)alarm.clone();
            updateDayOfMonthAndMonth( dayOfMonthAlarm );
            
            // take the earlier one
            if( dayOfMonthAlarm.getTime().getTime() < dayOfWeekAlarm.getTime().getTime() )
            {
                alarm = dayOfMonthAlarm;
                logger.debug( "after dayOfMonth CLOSER: " + alarm.getTime() );
            }
            else
            {
                alarm = dayOfWeekAlarm;
                logger.debug( "after dayOfWeek CLOSER: " + alarm.getTime() );
            }
        }
        else if( daysOfWeek[0] != -1 ) // only dayOfWeek is restricted
        {
            // update dayInWeek and month if necessary
            updateDayOfWeekAndMonth( alarm );
            logger.debug( "after dayOfWeek: " + alarm.getTime() );
        }
        else if( daysOfMonth[0] != -1 ) // only dayOfMonth is restricted
        {
            // update dayInMonth and month if necessary
            updateDayOfMonthAndMonth( alarm );
            logger.debug( "after dayOfMonth: " + alarm.getTime() );
        }
        // else if neither is restricted (both[0] == -1), we don't need to do anything.
        
        
        logger.debug("alarm: " + alarm.getTime());
        
        alarmTime = alarm.getTime().getTime();
    }
    
    /**
     * daysInMonth can't use simple offsets like the other fields, because the
     * number of days varies per month (think of an alarm that executes on every
     * 31st).  Instead we advance month and dayInMonth together until we're on a
     * matching value pair.
     */
    private void updateDayOfMonthAndMonth( Calendar alarm )
    {
        int currentMonth = alarm.get( Calendar.MONTH );
        int currentDayOfMonth = alarm.get( Calendar.DAY_OF_MONTH );
        int offset = 0;
        
        // loop until we have a valid day AND month (if current is invalid)
        while( !isIn(currentMonth, months) || !isIn(currentDayOfMonth, daysOfMonth) )
        {
            // if current month is invalid, advance to 1st day of next valid month
            if( !isIn(currentMonth, months) )
            {
                offset = getOffsetToNextOrEqual( currentMonth, MIN_MONTH, MAX_MONTH, months );
                alarm.add( Calendar.MONTH, offset );
                alarm.set( Calendar.DAY_OF_MONTH, 1 );
                currentDayOfMonth = 1;
            }
            
            // advance to the next valid day of month, if necessary
            if( !isIn(currentDayOfMonth, daysOfMonth) )
            {
                int maxDayOfMonth = alarm.getActualMaximum( Calendar.DAY_OF_MONTH );
                offset = getOffsetToNextOrEqual( currentDayOfMonth, MIN_DAY_OF_MONTH, maxDayOfMonth, daysOfMonth );
                alarm.add( Calendar.DAY_OF_MONTH, offset );
            }
            
            currentMonth = alarm.get( Calendar.MONTH );
            currentDayOfMonth = alarm.get( Calendar.DAY_OF_MONTH );
        }
    }
    
    
    private void updateDayOfWeekAndMonth( Calendar alarm )
    {
        int currentMonth = alarm.get( Calendar.MONTH );
        int currentDayOfWeek = alarm.get( Calendar.DAY_OF_WEEK );
        int offset = 0;
        
        // loop until we have a valid day AND month (if current is invalid)
        while( !isIn(currentMonth, months) || !isIn(currentDayOfWeek, daysOfWeek) )
        {
            // if current month is invalid, advance to 1st day of next valid month
            if( !isIn(currentMonth, months) )
            {
                offset = getOffsetToNextOrEqual( currentMonth, MIN_MONTH, MAX_MONTH, months );
                alarm.add( Calendar.MONTH, offset );
                alarm.set( Calendar.DAY_OF_MONTH, 1 );
                currentDayOfWeek = alarm.get( Calendar.DAY_OF_WEEK );
            }
            
            // advance to the next valid day of week, if necessary
            if( !isIn(currentDayOfWeek, daysOfWeek) )
            {
                offset = getOffsetToNextOrEqual( currentDayOfWeek, MIN_DAY_OF_WEEK, MAX_DAY_OF_WEEK, daysOfWeek );
                alarm.add( Calendar.DAY_OF_YEAR, offset );
            }
            
            currentDayOfWeek = alarm.get( Calendar.DAY_OF_WEEK );
            currentMonth = alarm.get( Calendar.MONTH );
        }
    }
    
    
    
    // ----------------------------------------------------------------------
    //                      General utility methods
    // ----------------------------------------------------------------------
    
    /**
     * if values = {-1}
     *   offset is 1 (because next value definitely matches)
     * if current < last(values)
     *   offset is diff to next valid value
     * if current >= last(values)
     *   offset is diff to values[0], wrapping from max to min
     */
    private static int getOffsetToNext( int current, int min, int max, int[] values )
    {
        int offset = 0;
        
        // find the distance to the closest valid value > current (wrapping if neccessary)
        
        // {-1} means *  -- offset is 1 because current++ is valid value
        if (values[0] == -1 )
        {
            offset = 1;
        }
        else
        {
            // need to wrap
            if( current >= last(values) )
            {
                int next = values[0];
                offset = (max-current+1) + (next-min);
            }
            else // current < max(values) -- find next valid value after current
            {
                findvalue:
                for( int i=0; i<values.length; i++ )
                {
                    if( current < values[i] )
                    {
                        offset = values[i] - current;
                        break findvalue;
                    }
                }
            } // end current < max(values)
        }
        
        return offset;
    }
    
    /**
     * if values = {-1} or current is valid
     *   offset is 0.
     * if current < last(values)
     *   offset is diff to next valid value
     * if current >= last(values)
     *   offset is diff to values[0], wrapping from max to min
     */
    private static int getOffsetToNextOrEqual( int current, int min, int max, int[] values )
    {
        int offset = 0;
        int[] safeValues = null;
        
        // find the distance to the closest valid value >= current (wrapping if necessary)
        
        // {-1} means *  -- offset is 0 if current is valid value
        if (values[0] == -1 || isIn(current, values) )
        {
            offset = 0;
        }
        else
        {
            safeValues = discardValuesOverMax( values, max );
            
            // need to wrap
            if( current > last(safeValues) )
            {
                int next = safeValues[0];
                offset = (max-current+1) + (next-min);
            }
            else // current <= max(values) -- find next valid value
            {
                findvalue:
                for( int i=0; i<values.length; i++ )
                {
                    if( current < safeValues[i] )
                    {
                        offset = safeValues[i] - current;
                        break findvalue;
                    }
                }
            } // end current <= max(values)
        }
        
        return offset;
    }
    
    /**
     * handles -1 in values as * and returns true
     * otherwise returns true iff given value is in the array
     */
    private static boolean isIn( int find, int[] values )
    {
        if( values[0] == -1 )
        {
            return true;
        }
        else
        {
            for( int i=0; i<values.length; i++ )
            {
                if( find == values[i] )
                    return true;
            }
            return false;
        }
    }
    
    /**
     * @return the last int in the array
     */
    private static int last( int[] intArray )
    {
        return intArray[ intArray.length - 1 ];
    }
    
    /**
     * Assumes inputted values are not null, have at least one value, and are in
     * ascending order.
     * @return  copy of values without any trailing values that exceed the max
     */
    private static int[] discardValuesOverMax( int[] values, int max )
    {
        int[] safeValues = null;
        for( int i=0; i<values.length; i++ )
        {
            if( values[i] > max )
            {
                safeValues = new int[i];
                System.arraycopy( values, 0, safeValues, 0, i );
                return safeValues;
            }
        }
        return values;
    }
    
}









