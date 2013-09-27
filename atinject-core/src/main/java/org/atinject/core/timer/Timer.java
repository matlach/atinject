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

import java.util.TimeZone;


public class Timer {
    
    private int[] seconds;
    private int[] minutes;
    private int[] hours;
    private int[] daysOfWeek;
    private int[] daysOfMonth;
    private int[] months;
    
    private int year; // no support for a list of years -- must be * or specified
    
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
     * @param year year of the alarm. When this field is not set (i.e. -1)
     * the alarm is repetitive (i.e. it is rescheduled when reached).
     * @param listener the alarm listener.
     * @return the AlarmEntry.
     * @exception PastDateException if the alarm date is in the past
     * (or less than 1 second away from the current date).
     */
    public Timer(int[] seconds, int[] minutes, int[] hours,
            int[] daysOfWeek,
            int[] daysOfMonth,
            int[] months,
            int year,
            String info,
            String timeZone) {

        this.seconds = seconds;
        this.minutes = minutes;
        this.months = months;
        this.hours = hours;
        this.daysOfWeek = daysOfWeek;
        this.daysOfMonth = daysOfMonth;
        this.year = year;
        
        this.info = info;
        this.timeZone = TimeZone.getTimeZone(timeZone);
    }

    public int[] getSeconds()
    {
        return seconds;
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

    public void setDaysOfWeek(int[] daysOfWeek)
    {
        this.daysOfWeek = daysOfWeek;
    }

    public int[] getDaysOfMonth()
    {
        return daysOfMonth;
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

    public int getYear()
    {
        return year;
    }

    public void setYear(int year)
    {
        this.year = year;
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









