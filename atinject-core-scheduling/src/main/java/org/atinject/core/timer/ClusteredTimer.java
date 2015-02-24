package org.atinject.core.timer;

public class ClusteredTimer extends Timer {

    public String cacheName;
    public String key;
    
    public ClusteredTimer(int[] seconds, int[] minutes, int[] hours, int[] daysOfWeek, int[] daysOfMonth, int[] months,
            String info, String timeZone, String cacheName, String key) {
        super(seconds, minutes, hours, daysOfWeek, daysOfMonth, months, info, timeZone);
        
        this.cacheName = cacheName;
        this.key = key;
    }

}
