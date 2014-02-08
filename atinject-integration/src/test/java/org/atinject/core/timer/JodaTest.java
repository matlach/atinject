package org.atinject.core.timer;

import java.util.Arrays;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.junit.Test;

public class JodaTest {

    @Test
    public void test(){
        int[] seconds = {30}; int[] minutes = {}; int[] hours = {};
        int[] daysOfWeek = {}; int[] daysOfMonth = {}; int[] months = {}; int[] years = {};
        
        DateTime now = DateTime.now();
        DateTime nowRoundedToTheNextSecond = new DateTime(now).withMillisOfSecond(0).plusSeconds(1);
        MutableDateTime nextTime = new MutableDateTime(nowRoundedToTheNextSecond);
        
        System.out.println(now);
        System.out.println(nowRoundedToTheNextSecond);
        
        updateSeconds(nextTime, seconds);
        System.out.println(nextTime);
        updateMinutes(nextTime, minutes);
        System.out.println(nextTime);
        updateHours(nextTime, hours);
        System.out.println(nextTime);
        
        updateMonths(nextTime, months);
        updateYears(nextTime, years);
        
        long scheduleNextInMillis = nextTime.getMillis() - now.getMillis();
        System.out.println(scheduleNextInMillis);
    }
    
    void updateSeconds(MutableDateTime nextTime, int[] seconds) {
        nextTime.addSeconds(getOffsetToNext(nextTime.getSecondOfMinute(), 0, 59, seconds));
    }
    
    void updateMinutes(MutableDateTime nextTime, int[] minutes) {
        nextTime.addMinutes(getOffsetToNextOrEqual(nextTime.getMinuteOfHour(), 0, 59, minutes));
    }
    
    void updateHours(MutableDateTime nextTime, int[] hours) {
        nextTime.addHours(getOffsetToNextOrEqual(nextTime.getHourOfDay(), 0, 23, hours));
    }
    
    void updateMonths(MutableDateTime nextTime, int[] months) {
        nextTime.addMonths(getOffsetToNextOrEqual(nextTime.getMonthOfYear() - 1, 0, 11, months));
    }
    
    void updateYears(MutableDateTime nextTime, int[] years) {
        nextTime.addYears(getOffsetToNextOrEqual(nextTime.getYear(), 2000, 2100, years));
    }
    
    int getOffsetToNext(int current, int min, int max, int[] values) {
        if (values.length == 0) {
            return 1;
        }
        if (current >= last(values)) {
            return (max - current + 1) + (first(values) - min);
        }
        // TODO binary search 
        for (int value : values) {
            if (value >= current){
                return value - current;
            }
        }
        throw new AssertionError();
    }
    
    int getOffsetToNextOrEqual(int current, int min, int max, int[] values) {
        if (values.length == 0 || isIn(values, current)) {
            return 0;
        }
        if (current >= last(values)) {
            return (max - current + 1) + (first(values) - min);
        }
        for (int value : values) {
            if (value >= current){
                return value - current;
            }
        }
        throw new AssertionError();
    }
    
    boolean isIn(int[] a, int key) {
        return Arrays.binarySearch(a, key) >= 0;
    }
    
    int last(int[] a) {
        return a[a.length - 1];
    }
    
    int first(int[] a) {
        return a[0];
    }
    
}
