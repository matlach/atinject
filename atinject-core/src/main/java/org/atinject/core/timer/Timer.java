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

package org.atinject.core.timer;

import java.util.Arrays;
import java.util.TimeZone;


public class Timer {
    
    private int[] seconds;
    private int[] minutes;
    private int[] hours;
    private int[] daysOfWeek;
    private int[] daysOfMonth;
    private int[] months;
    
    public long alarmTime;
    
    public TimeZone timeZone;
    public String info;
    
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
     * @return the AlarmEntry.
     * @exception PastDateException if the alarm date is in the past
     * (or less than 1 second away from the current date).
     */
    public Timer(
    		int[] seconds, int[] minutes, int[] hours,
            int[] daysOfWeek, int[] daysOfMonth, int[] months,
            String info, String timeZone) {

        this.seconds = validateAndSort(0, 59, seconds);
        this.minutes = validateAndSort(0, 59, minutes);
        this.months = validateAndSort(0, 11, months);
        this.hours = validateAndSort(0, 23, hours);
        this.daysOfWeek = validateAndSort(1, 7, daysOfWeek);
        this.daysOfMonth = validateAndSort(1, 31, daysOfMonth);
        
        this.info = info;
        this.timeZone = TimeZone.getTimeZone(timeZone);
    }
    
    private int[] validateAndSort(int min, int max, int[] array) {
    	Arrays.sort(array);
    	if (array[0] != -1) {
    		if (array[0] < min) {
    			throw new RuntimeException();
    		}
    		if (array[array.length-1] > max) {
    			throw new RuntimeException();
    		}
    	}
    	return array;
    }

    public int[] getSeconds()
    {
        return seconds;
    }
    
    public boolean isValidForEachSeconds() {
    	return seconds[0] == -1;
    }

    public void setSeconds(int[] seconds)
    {
        this.seconds = seconds;
    }

    public int[] getMinutes()
    {
        return minutes;
    }

    public void setMinutes(int[] minutes)
    {
        this.minutes = minutes;
    }

    public int[] getHours()
    {
        return hours;
    }

    public void setHours(int[] hours)
    {
        this.hours = hours;
    }

    public int[] getDaysOfWeek()
    {
        return daysOfWeek;
    }
    
    public boolean isValidForEachDayOfWeek() {
    	return daysOfWeek[0] == -1;
    }
    
    public boolean isDayOfWeekRestricted() {
    	return !isValidForEachDayOfWeek();
    }
    
    public void setDaysOfWeek(int[] daysOfWeek)
    {
        this.daysOfWeek = daysOfWeek;
    }

    public int[] getDaysOfMonth()
    {
        return daysOfMonth;
    }
    
    public boolean isDayOfMonthRestricted() {
    	return daysOfMonth[0] != -1;
    }

    public void setDaysOfMonth(int[] daysOfMonth)
    {
        this.daysOfMonth = daysOfMonth;
    }

    public int[] getMonths()
    {
        return months;
    }

    public void setMonths(int[] months)
    {
        this.months = months;
    }

    public long getAlarmTime()
    {
        return alarmTime;
    }

    public void setAlarmTime(long alarmTime)
    {
        this.alarmTime = alarmTime;
    }
    
}
